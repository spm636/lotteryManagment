package com.example.lottery.service;

import com.example.lottery.dto.DailyTicketReport;
import com.example.lottery.dto.Execepted;
import com.example.lottery.dto.TicketCount;
import com.example.lottery.dto.TicketDetailsDto;
import com.example.lottery.model.ErrorMessage;
import com.example.lottery.model.LimitExceedData;
import com.example.lottery.model.TicketDetails;
import com.example.lottery.model.TicketRate;
import com.example.lottery.repository.ErrorMessageRepository;
import com.example.lottery.repository.TicketDetailsRepository;
import com.example.lottery.repository.TicketLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TicketDetailsServiceImpl implements TicketDetailsService {

    TicketDetailsRepository ticketDetailsRepository;

    @Autowired
    ErrorMessageRepository errorMessageRepository;
    @Autowired
    TicketLimitRepository ticketLimitRepository;
    @Autowired
    TicketRateService ticketRateService;

    @Autowired
    public TicketDetailsServiceImpl(TicketDetailsRepository ticketDetailsRepository) {
        this.ticketDetailsRepository = ticketDetailsRepository;
    }
    List<String> dataList=new ArrayList<>();
    @Override
    public void saveTicketDetails(TicketDetailsDto ticketDetailsDto) {


        TicketRate ticketRate=ticketRateService.findByid(1L);

        String ticket = ticketDetailsDto.getEnterDetails();
        Scanner scanner = new Scanner(ticket);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine(); // Read a line of input


            String[] data = new String[]{line};


            for (String n : data) {
               // n = n.replaceAll("\\[\\d{1,2}:\\d{2} (am|pm), \\d{1,2}/\\d{1,2}/\\d{4}\\] (.+): ", "");
              //  n = n.replaceAll("\\[\\d{2}/\\d{2}/\\d{4}\\,\\d{1,2}:\\d{2} (am|pm)] (.+): ", "");
               // n = n.replaceAll("\\d{2}/\\d{2}/\\d{2}, \\d{1,2}:\\d{2}\\s?(am|pm)? - \\+(.+):", "");
                // n = n.replaceAll("\\D^*?(?=a|b|c)", "");
                n=n.replaceAll("\\[.*?\\] (.+): ", "");
                StringBuilder sb = new StringBuilder();
                char prevChar = '\0'; // Initialize with a character that won't be in the string
                boolean prevIsSpecial = false;
                for (char c : n.toCharArray()) {
                    boolean isSpecial = !Character.isLetterOrDigit(c) || Character.isWhitespace(c);
                    //if (!isSpecial || !prevIsSpecial || !Character.isWhitespace(prevChar) ) {
                    if (!isSpecial || !prevIsSpecial ){
                        // Append if current character is not a special character,
                        // or if it's not consecutive, or if it's different from previous one,
                        // or if it's whitespace (to keep only one whitespace)
                        sb.append(c);
                        prevChar = c;
                        prevIsSpecial = isSpecial;
                    }
                }




                n = sb.toString();
                if (n.contains("d") || n.contains("D") || n.contains("k") || n.contains("K")) {
                    char sp = 0;
                    for (char c : n.toCharArray()) {
                        if (!Character.isLetterOrDigit(c)) {
                            sp = c;
                            break;
                        }
                    }
                    String regex = "(D|d|K|k)\\w*" + Pattern.quote(String.valueOf(sp));
                    n = n.replaceAll(regex, String.valueOf(sp));
                    // Replace leading or trailing non-word characters with a single space
                    n = n.replaceAll("^\\W+|\\W+$", " ").trim();

                }
                // if(!n.contains("a") || !n.contains("b") ||!n.contains("c")) {
                int specialCharCount = 0;
                char sp = 0;
                char prev=0;

                for (char c : n.toCharArray()) {
                    //&& !Character.isWhitespace(c)
                    if (!Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                        specialCharCount++;
                        prev=sp;
                        sp = c;
                    }
                }
                n=removestartspc(n);
                if (prev==sp || n.contains("to")|| n.contains("To") || n.contains("TO")){
                    ErrorMessage errorMessage = new ErrorMessage();
                    errorMessage.setCoupen(n);
                    errorMessage.setTicket(ticketDetailsDto.getTicket());
                    errorMessageRepository.save(errorMessage);
                }


               else if (specialCharCount > 1 && !n.matches(".*[a-zA-Z].*")) {
                    if ((n.indexOf(sp) != -1 && n.contains(" ")) || (n.indexOf(sp) == -1 && n.contains(" ")) ){
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setCoupen(n);
                        errorMessage.setTicket(ticketDetailsDto.getTicket());
                        errorMessageRepository.save(errorMessage);
                    }
                    else {
                        int firstIndex = n.indexOf(sp);
                        int secondIndex = n.indexOf(sp, firstIndex + 1);


                        // Process each part as needed
                        while (secondIndex != -1) {
                            String part = n.substring(0, secondIndex + 1);
                            int lastIndex = part.lastIndexOf(sp);
                            part = part.substring(0, lastIndex);
                            System.out.println(part);
                            // Now part contains each separated value
                            if (getNewCount(part) <= ticketDetailsDto.getLimit()) {
                                if (getNewCount(part) == 0) {
                                    ErrorMessage errorMessage = new ErrorMessage();
                                    errorMessage.setCoupen(part);
                                    errorMessage.setTicket(ticketDetailsDto.getTicket());
                                    errorMessageRepository.save(errorMessage);


                                } else if (getNewCoupon(part).length() == 3) {
                                    TicketDetails ticketDetail = new TicketDetails();
                                    ticketDetail.setName(ticketDetailsDto.getName());
                                    ticketDetail.setDate(ticketDetailsDto.getDate());
                                    ticketDetail.setTicket(ticketDetailsDto.getTicket());


                                    ticketDetail.setCoupenNumber(getNewCoupon(part));
                                    ticketDetail.setCount(getNewCount(part));
                                    ticketDetail.setType("super");
                                    ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));


                                    ticketDetailsRepository.save(ticketDetail);
                                }
                            } else {
                                if (getNewCoupon(part).length() == 3) {
                                    TicketDetails ticketDetail = new TicketDetails();
                                    LimitExceedData limitExceedData = new LimitExceedData();
                                    ticketDetail.setName(ticketDetailsDto.getName());
                                    limitExceedData.setName(ticketDetailsDto.getName());
                                    ticketDetail.setDate(ticketDetailsDto.getDate());
                                    limitExceedData.setDate(ticketDetailsDto.getDate());
                                    ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                    limitExceedData.setTicket(ticketDetailsDto.getTicket());


                                    ticketDetail.setCoupenNumber(getNewCoupon(part));
                                    ticketDetail.setCount(ticketDetailsDto.getLimit());
                                    ticketDetail.setType("super");
                                    ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));

                                    limitExceedData.setCoupenNumber(getNewCoupon(part));
                                    limitExceedData.setCount(getNewCount(part) - ticketDetailsDto.getLimit());
                                    limitExceedData.setType("super");
                                    limitExceedData.setRate((double) (limitExceedData.getCount() * ticketRate.getSuper1()));


                                    ticketDetailsRepository.save(ticketDetail);
                                    ticketLimitRepository.save(limitExceedData);
                                }

                            }


                            n = n.substring(secondIndex + 1);

                            // Find the index of the next second occurrence of '%'
                            firstIndex = n.indexOf(sp);
                            secondIndex = n.indexOf(sp, firstIndex + 1);
                        }

                        if (getNewCount(n) <= ticketDetailsDto.getLimit()) {
                            if (getNewCount(n) == 0) {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setCoupen(n);
                                errorMessage.setTicket(ticketDetailsDto.getTicket());
                                errorMessageRepository.save(errorMessage);

                            } else if (getNewCoupon(n).length() == 3) {
                                TicketDetails ticketDetail = new TicketDetails();
                                ticketDetail.setName(ticketDetailsDto.getName());
                                ticketDetail.setDate(ticketDetailsDto.getDate());
                                ticketDetail.setTicket(ticketDetailsDto.getTicket());


                                ticketDetail.setCoupenNumber(getNewCoupon(n));
                                ticketDetail.setCount(getNewCount(n));
                                ticketDetail.setType("super");
                                ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));


                                ticketDetailsRepository.save(ticketDetail);

                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setCoupen(n);
                                errorMessage.setTicket(ticketDetailsDto.getTicket());
                                errorMessageRepository.save(errorMessage);


                            }


                        } else {
                            if (getNewCoupon(n).length() == 3) {
                                TicketDetails ticketDetail = new TicketDetails();
                                LimitExceedData limitExceedData = new LimitExceedData();
                                ticketDetail.setName(ticketDetailsDto.getName());
                                limitExceedData.setName(ticketDetailsDto.getName());
                                ticketDetail.setDate(ticketDetailsDto.getDate());
                                limitExceedData.setDate(ticketDetailsDto.getDate());
                                ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                limitExceedData.setTicket(ticketDetailsDto.getTicket());


                                ticketDetail.setCoupenNumber(getNewCoupon(n));
                                ticketDetail.setCount(ticketDetailsDto.getLimit());
                                ticketDetail.setType("super");
                                ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));

                                limitExceedData.setCoupenNumber(getNewCoupon(n));
                                limitExceedData.setCount(getNewCount(n) - ticketDetailsDto.getLimit());
                                limitExceedData.setType("super");
                                limitExceedData.setRate((double) (limitExceedData.getCount() * ticketRate.getSuper1()));


                                ticketDetailsRepository.save(ticketDetail);
                                ticketLimitRepository.save(limitExceedData);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setCoupen(n);
                                errorMessage.setTicket(ticketDetailsDto.getTicket());
                                errorMessageRepository.save(errorMessage);


                            }

                        }
                    }

                }




                else {
                    int specialCharCount1 = 0;
                    char sp1 = 0;
                    for (char c : n.toCharArray()) {
                        if (!Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                            specialCharCount1++;
                            sp1 = c;
                        }
                    }
//                    if(specialCharCount1==2){
//                        n = n.replaceFirst(Pattern.quote(String.valueOf(sp1)), "");
//
//                    }
                    int spz=0;
                    for(char c:n.toCharArray()){
                        if (!Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                            spz++;

                        }

                    }


                    if (spz > 1) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setCoupen(n);
                        errorMessage.setTicket(ticketDetailsDto.getTicket());
                        errorMessageRepository.save(errorMessage);

                    } else {
//                        if ((n.indexOf(sp) != -1 && n.contains(" ")) || (n.indexOf(sp) == -1 && n.contains(" ")) ){
//                            ErrorMessage errorMessage = new ErrorMessage();
//                            errorMessage.setCoupen(n);
//                            errorMessage.setTicket(ticketDetailsDto.getTicket());
//                            errorMessageRepository.save(errorMessage);
//                        }

                       if (getNewCoupon(n).length() == 3 && !n.contains("abc")) {
                            if (getNewCount(n) <= ticketDetailsDto.getLimit()) {
                                if (getNewCount(n) < 1 ) {
                                    ErrorMessage errorMessage = new ErrorMessage();
                                    errorMessage.setCoupen(n);
                                    errorMessage.setTicket(ticketDetailsDto.getTicket());
                                    errorMessageRepository.save(errorMessage);

                                } else {
                                    TicketDetails ticketDetail = new TicketDetails();
                                    ticketDetail.setName(ticketDetailsDto.getName());
                                    ticketDetail.setDate(ticketDetailsDto.getDate());
                                    ticketDetail.setTicket(ticketDetailsDto.getTicket());

                                    if (n.contains("box") || n.contains("b")  || n.contains("B") ) {
                                        ticketDetail.setCoupenNumber(getNewCoupon(n));
                                        String first=getNewCoupon(n);
                                        List<String> frst = new ArrayList<>();

                                        generatePermutations(first.toCharArray(), 0, frst);
                                        Set<String> set = new HashSet<>(frst);
                                        int size=set.size();
                                        ticketDetail.setCount(getNewCount(n));
                                        ticketDetail.setType("box");
                                        ticketDetail.setRate((double) (size*ticketDetail.getCount() * ticketRate.getBox()));

                                    } else if (getNewCoupon(n).length() == 3) {
                                        ticketDetail.setCoupenNumber(getNewCoupon(n));
                                        ticketDetail.setCount(getNewCount(n));
                                        ticketDetail.setType("super");
                                        ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));

                                    }

                                    ticketDetailsRepository.save(ticketDetail);

                                }
                            } else {
                                TicketDetails ticketDetail = new TicketDetails();
                                LimitExceedData limitExceedData = new LimitExceedData();
                                ticketDetail.setName(ticketDetailsDto.getName());
                                limitExceedData.setName(ticketDetailsDto.getName());
                                ticketDetail.setDate(ticketDetailsDto.getDate());
                                limitExceedData.setDate(ticketDetailsDto.getDate());
                                ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                limitExceedData.setTicket(ticketDetailsDto.getTicket());

                                if ((n.contains("box") || (n.contains("b") || n.contains("B")) && getCoupen(n).length() == 3) && getNewCount(n) > 0) {
                                    ticketDetail.setCoupenNumber(getNewCoupon(n));


                                    String first=getNewCoupon(n);
                                    List<String> frst = new ArrayList<>();

                                    generatePermutations(first.toCharArray(), 0, frst);
                                    Set<String> set = new HashSet<>(frst);
                                    int size=set.size();
                                    ticketDetail.setCount(ticketDetailsDto.getLimit());
                                    ticketDetail.setType("box");
                                    ticketDetail.setRate((double) (size*ticketDetail.getCount() * ticketRate.getBox()));

                                    limitExceedData.setCoupenNumber(getCoupen(n));


                                    limitExceedData.setCount((getNewCount(n)) - ticketDetailsDto.getLimit());
                                    limitExceedData.setType("box");
                                    limitExceedData.setRate((double) (size*limitExceedData.getCount() * ticketRate.getBox()));
                                } else if (getNewCoupon(n).length() == 3) {
                                    ticketDetail.setCoupenNumber(getNewCoupon(n));
                                    ticketDetail.setCount(ticketDetailsDto.getLimit());
                                    ticketDetail.setType("super");
                                    ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));

                                    limitExceedData.setCoupenNumber(getNewCoupon(n));
                                    limitExceedData.setCount(getNewCount(n) - ticketDetailsDto.getLimit());
                                    limitExceedData.setType("super");
                                    limitExceedData.setRate((double) (limitExceedData.getCount() * ticketRate.getSuper1()));

                                }


                                ticketDetailsRepository.save(ticketDetail);
                                ticketLimitRepository.save(limitExceedData);
                            }
                        }



                    else {
                            if (getCount(n) <= ticketDetailsDto.getLimit()) {
                                if ((n.contains("a") || n.contains("b") || n.contains("c") || n.contains("A") || n.contains("B") || n.contains("C")) && getCount(n) > 0) {

                                    if(digitCount(getBlockCoupen(n))==1 && countLetters(getBlockCoupen(n))==1) {
                                        TicketDetails ticketDetail = new TicketDetails();
                                        ticketDetail.setName(ticketDetailsDto.getName());
                                        ticketDetail.setDate(ticketDetailsDto.getDate());
                                        ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                        ticketDetail.setCoupenNumber(getBlockCoupen(n));
                                        ticketDetail.setCount(getCount(n));
                                        ticketDetail.setType("block");

                                        ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getBlock()));
                                        ticketDetailsRepository.save(ticketDetail);
                                    }
                                   else if(digitCount(getBlockCoupen(n))==1 && countLetters(getBlockCoupen(n))==2) {
                                        TicketDetails ticketDetail = new TicketDetails();
                                        ticketDetail.setName(ticketDetailsDto.getName());
                                        ticketDetail.setDate(ticketDetailsDto.getDate());
                                        ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                        ticketDetail.setCoupenNumber(getBlockCoupen(n));
                                        ticketDetail.setCount(getCount(n));
                                       ticketDetail.setType("block");

                                        ticketDetail.setRate((double) (2*ticketDetail.getCount() * ticketRate.getBlock()));
                                        ticketDetailsRepository.save(ticketDetail);
                                    }
                                    else if(digitCount(getBlockCoupen(n))==1 && countLetters(getBlockCoupen(n))==3) {
                                        TicketDetails ticketDetail = new TicketDetails();
                                        ticketDetail.setName(ticketDetailsDto.getName());
                                        ticketDetail.setDate(ticketDetailsDto.getDate());
                                        ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                        ticketDetail.setCoupenNumber(getBlockCoupen(n));
                                        ticketDetail.setCount(getCount(n));
                                        ticketDetail.setType("block");

                                        ticketDetail.setRate((double) (3*ticketDetail.getCount() * ticketRate.getBlock()));
                                        ticketDetailsRepository.save(ticketDetail);
                                    }
                                    else if(digitCount(getBlockCoupen(n))==2 && countLetters(getBlockCoupen(n))==2) {
                                        TicketDetails ticketDetail = new TicketDetails();
                                        ticketDetail.setName(ticketDetailsDto.getName());
                                        ticketDetail.setDate(ticketDetailsDto.getDate());
                                        ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                        ticketDetail.setCoupenNumber(getBlockCoupen(n));
                                        ticketDetail.setCount(getCount(n));
                                        ticketDetail.setType("super");

                                        ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));
                                        ticketDetailsRepository.save(ticketDetail);
                                    }
                                    else if(digitCount(getBlockCoupen(n))==3 && countLetters(getBlockCoupen(n))==3) {
                                        TicketDetails ticketDetail = new TicketDetails();
                                        ticketDetail.setName(ticketDetailsDto.getName());
                                        ticketDetail.setDate(ticketDetailsDto.getDate());
                                        ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                        ticketDetail.setCoupenNumber(getBlockCoupen(n));
                                        ticketDetail.setCount(getCount(n));
                                        ticketDetail.setType("super");

                                        ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getSuper1()));
                                        ticketDetailsRepository.save(ticketDetail);
                                    }



                                } else {
                                    ErrorMessage errorMessage = new ErrorMessage();
                                    errorMessage.setCoupen(n);
                                    errorMessage.setTicket(ticketDetailsDto.getTicket());
                                    errorMessageRepository.save(errorMessage);

                                }
                            } else {
                                if ((n.contains("a") || n.contains("b") || n.contains("c") || n.contains("A") || n.contains("B") || n.contains("C")) && getCount(n) > 0) {
                                    TicketDetails ticketDetail = new TicketDetails();
                                    ticketDetail.setName(ticketDetailsDto.getName());
                                    ticketDetail.setDate(ticketDetailsDto.getDate());
                                    ticketDetail.setTicket(ticketDetailsDto.getTicket());
                                    ticketDetail.setCoupenNumber(getBlockCoupen(n));
                                    ticketDetail.setCount(ticketDetailsDto.getLimit());
                                    ticketDetail.setType("block");
                                    ticketDetail.setRate((double) (ticketDetail.getCount() * ticketRate.getBlock()));
                                    ticketDetailsRepository.save(ticketDetail);

                                    LimitExceedData limitExceedData = new LimitExceedData();
                                    limitExceedData.setName(ticketDetailsDto.getName());
                                    limitExceedData.setDate(ticketDetailsDto.getDate());
                                    limitExceedData.setTicket(ticketDetailsDto.getTicket());
                                    limitExceedData.setCoupenNumber(getBlockCoupen(n));
                                    limitExceedData.setCount(getCount(n) - ticketDetailsDto.getLimit());
                                    limitExceedData.setType("block");
                                    limitExceedData.setRate((double) (limitExceedData.getCount() * ticketRate.getBlock()));
                                    ticketLimitRepository.save(limitExceedData);

                                } else {
                                    ErrorMessage errorMessage = new ErrorMessage();
                                    errorMessage.setCoupen(n);
                                    errorMessage.setTicket(ticketDetailsDto.getTicket());
                                    errorMessageRepository.save(errorMessage);

                                }


                            }

                        }
                    }
                }
            }
        }

    }
    public static String findFirstThreeDigits(String input) {
        Pattern pattern = Pattern.compile("\\d{3}");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null; // Return null if no match is found
    }

    @Override
    public List<ErrorMessage> findActiveError(Long id) {
        return errorMessageRepository.findByAcivated(id);
    }

    @Override
    public List<String> ErrorData() {
        return dataList;
    }
    public static int countLetters(String input) {
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            // Check if the character is a letter
            if (Character.isLetter(c)) {
                count++;
            }
        }
        return count;
    }

    private static int digitCount(String input) {
        // Use regular expression to remove all non-digit characters

        input=input.replaceAll("[^0-9]", "");
        return input.length();
    }

    private int getCount(String data) {
        char[] separators = {'-', '+', '*','.',' ','=','/','%','?'};

        // Initialize count to 0
        int count = 0;

        // Iterate through each separator character
        for (char separator : separators) {
            // Find the last occurrence of the separator character
            int index = data.lastIndexOf(separator);
            // If the separator character is found
            if (index != -1) {
                // Extract the substring after the separator character
                String countPart = data.substring(index + 1);
                // Parse the substring to integer
                try {
                    count = Integer.parseInt(countPart);
                } catch (NumberFormatException e) {
                    // Handle parsing error, e.g., invalid input format
                    e.printStackTrace();
                }
                // Exit the loop since we found the count value
                break;
            }
        }
        return count;
    }
    public static String removestartspc(String inputString) {
        Pattern pattern = Pattern.compile("^\\W+");
        Matcher matcher = pattern.matcher(inputString);
        // Replace matched leading characters with empty string
        String cleanedString = matcher.replaceFirst("");
        return cleanedString;
    }
    private static int getNewCount(String input) {
        // Remove all non-digit characters from the string
//        String digitsOnly = input.replaceAll("[^0-9]", "");
//
//        // Find the first occurrence of three consecutive digits
//        Pattern pattern = Pattern.compile("\\d{3}");
//        Matcher matcher = pattern.matcher(digitsOnly);
//
//        int newCount = 0;
//        if (matcher.find()) {
//            // Extract the substring after the first occurrence of three consecutive digits
//            String substring = digitsOnly.substring(matcher.end());
//            // Validate if substring is not empty and only contains digits
//            if (!substring.isEmpty() && substring.matches("\\d+")) {
//                // Parse the substring to get the new count
//                newCount = Integer.parseInt(substring);
//            }
//        }
//        return newCount;
        String regex = "[^0-9]+";

        // Split the input string using the regular expression pattern
        String[] splitStrings = input.split(regex);
        if(splitStrings.length>1) {
            int count = Integer.parseInt(splitStrings[1]);

            return count;
        }
        return 0;

    }
    private static void generatePermutations(char[] chars, int index, List<String> result) {
        if (index == chars.length - 1) {
            result.add(new String(chars));
            return;
        }

        for (int i = index; i < chars.length; i++) {
            swap(chars, index, i);
            generatePermutations(chars, index + 1, result);
            swap(chars, index, i);
        }
    }
    public static void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }

    private String getCoupen(String data) {
        //String coupen = data.replaceAll("[^\\d].*$", "");
        //int val=Integer.parseInt(coupen);
        String coupen = data.replaceAll("^.*?(\\d+).*", "$1");
        return coupen;
    }
    private String getBlockCoupen(String data) {
        Pattern pattern = Pattern.compile("([A-Za-z]+)[\\s\\W]*(\\d+)");
        Matcher matcher = pattern.matcher(data);

        if (matcher.find()) {
            String letters = matcher.group(1);
            String digits = matcher.group(2);

            // Capitalize the first letter of the letters part
            //char firstLetter = Character.toUpperCase(letters.charAt(0));
            String capitalizedLetters = letters.substring(0);

            return capitalizedLetters + digits;
        }

        return ""; // Return empty string if no match found
    }

    private static String getNewCoupon(String data) {
        // Use a regular expression to find the first sequence of digits
//        Pattern pattern = Pattern.compile("\\d+");
//        Matcher matcher = pattern.matcher(data);
//
//        String coupon = "";
//        if (matcher.find()) {
//            String digitsOnly = matcher.group(); // Get the first group (the sequence of digits)
//            if (digitsOnly.length() >= 3) {
//                coupon = digitsOnly.substring(0, 3); // Return the first three digits if there are three or more
//            } else {
//                coupon = digitsOnly; // Return all available digits if less than three
//            }
//        }
//        return coupon;
        String coupen = data.replaceAll("^.*?(\\d+).*", "$1");
        return coupen;
    }


    @Override
    public List<TicketDetails> findAllticketDetails() {
        return ticketDetailsRepository.findAll();
    }

    @Override
    public void delete(Long id) {
    ticketDetailsRepository.deleteById(id);
    }

    @Override
    public List<TicketCount> ticketCount(LocalDate date,Long id) {
        List<Object[]> objects=ticketDetailsRepository.ticketCount(date,id);
            List<TicketCount> result=new ArrayList<>();
            for(Object[] row:objects){

                String type=(String) row[0];
                Long count = (Long) row[1]; // Cast to Long instead of Integer
                int totalCount = count.intValue();
                result.add(new TicketCount(type,totalCount));
            }
        return result;

    }

    @Override
    public List<DailyTicketReport> dailyticketReport(LocalDate date, Long id) {
        List<Object[]> objects=ticketDetailsRepository.dailyTicketDetails(date,id);
        List<DailyTicketReport> reports=new ArrayList<>();
        for (Object[] row:objects){
            String Type=(String) row[0];
            Long count = (Long) row[1]; // Cast to Long instead of Integer
            int totelCount = count.intValue();
            Double amount=(Double) row[2];
            reports.add(new DailyTicketReport(Type,totelCount,amount));
        }
        return reports;
    }

    @Override
    public void deleteSelected(Long[] id) {
        for(long cid:id){
            TicketDetails ticketDetails=ticketDetailsRepository.getReferenceById(cid);
            ticketDetails.setActivated(false);
            ticketDetailsRepository.save(ticketDetails);
        }
    }

    @Override
    public void deleteError(Long[] id) {
        for (long cid:id){
            ErrorMessage errorMessage=errorMessageRepository.getReferenceById(cid);
            errorMessage.setAcivated(false);
            errorMessageRepository.save(errorMessage);
        }
    }

    @Override
    public List<TicketDetails> findByDateAndTicket(Long id, LocalDate date) {
        return ticketDetailsRepository.findTicketDetailsByDateAndTicket(id,date);
    }

    @Override
    public Double findTotelCoupenAmount(LocalDate date, Long id) {
        return ticketDetailsRepository.findTotelCoupenAmount(date,id);
    }

    @Override
    public List<TicketDetails> findTicketDetailsBykeyWord(Long id, LocalDate date, String keyword) {
        return ticketDetailsRepository.findTicketDetailsByKeyword(id,date,keyword);
    }
}
