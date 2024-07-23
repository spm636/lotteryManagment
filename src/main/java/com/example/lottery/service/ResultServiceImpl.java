package com.example.lottery.service;

import com.example.lottery.dto.DailyResult;
import com.example.lottery.dto.DrawDto4;
import com.example.lottery.model.LimitExceedData;
import com.example.lottery.model.PrizeAmount;
import com.example.lottery.model.Result;
import com.example.lottery.model.TicketDetails;
import com.example.lottery.repository.ResultRepository;
import com.example.lottery.repository.TicketLimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ResultServiceImpl implements ResultService{

    @Autowired
    ResultRepository resultRepository;
    @Autowired
    TicketDetailsService ticketDetailsService;
    @Autowired
    TicketLimitRepository ticketLimitRepository;
    @Autowired
    PrizeAmountService prizeAmountService;
    @Override
    public void saveResult(DrawDto4 drawDto4) {
        PrizeAmount prize=prizeAmountService.findByid(1L);
        Result result=null;

        String drawResult=drawDto4.getResult();
        drawResult=drawResult.replaceAll("[^\\d\\s]", "");

        String first=null;
        String second=null;
        String third=null;
        String fourth=null;
        String fifth=null;
        int index=0;
        String allResult[]=null;

        String[] data = drawResult.trim().split("\\s+");
        String complimentary[]=new String[data.length-5];
        first=data[0];
        second=data[1];
        third=data[2];
        fourth=data[3];
        fifth=data[4];
        for (int i=5;i<data.length;i++){
            if(data[i]!=null) {
                complimentary[index] = data[i];
                index++;


            }
        }
        long id=drawDto4.getTicket().getId();
        LocalDate date=drawDto4.getDate();
        List<TicketDetails> ticketDetails=ticketDetailsService.findByDateAndTicket(id,date);
        for(TicketDetails n:ticketDetails){
            List<Result> re=resultRepository.findAll();
            boolean existsInResult = re.stream().anyMatch(res -> res.getTicketDetails().equals(n));
            if (!existsInResult) {
                if (n.getType().contains("super") && (n.getCoupenNumber().contains("a")||n.getCoupenNumber().contains("b")||n.getCoupenNumber().contains("c")||
                n.getCoupenNumber().contains("A")||n.getCoupenNumber().contains("B")||n.getCoupenNumber().contains("C"))) {
                    String digit=removeNonDigits(n.getCoupenNumber());
                   if(digit.length()==3&&(n.getCoupenNumber().contains("Abc")||n.getCoupenNumber().contains("ABC")||n.getCoupenNumber().contains("abc"))) {
                      if(first.contains(digit)) {
                          result = new Result();
                          result.setTicketDetails(n);
                          result.setTicket(drawDto4.getTicket());
                          result.setPlace("first");
                          result.setTotelPrice((double) (n.getCount() * 700));
                          resultRepository.save(result);
                      }
                   }
                   else if(digit.length()==2&&(n.getCoupenNumber().contains("Ab")||n.getCoupenNumber().contains("AB")||n.getCoupenNumber().contains("ab"))) {
                       String frst=findFirstTwoDigits(first);
                       if(frst.contains(digit)) {
                           result = new Result();
                           result.setTicketDetails(n);
                           result.setTicket(drawDto4.getTicket());
                           result.setPlace("first");
                           result.setTotelPrice((double) (n.getCount() * 700));
                           resultRepository.save(result);
                       }
                   }
                   else if(digit.length()==2&&(n.getCoupenNumber().contains("AC")||n.getCoupenNumber().contains("Ac")||n.getCoupenNumber().contains("ac"))) {
                       String frst=findFirstAndLastDigits(first);
                       if(frst.contains(digit)) {
                           result = new Result();
                           result.setTicketDetails(n);
                           result.setTicket(drawDto4.getTicket());
                           result.setPlace("first");
                           result.setTotelPrice((double) (n.getCount() * 700));
                           resultRepository.save(result);
                       }
                   }
                   else if(digit.length()==2&&(n.getCoupenNumber().contains("BC")||n.getCoupenNumber().contains("Bc")||n.getCoupenNumber().contains("bc"))) {
                       String frst=findLastTwoNumber(first);
                       if(frst.contains(digit)) {
                           result = new Result();
                           result.setTicketDetails(n);
                           result.setTicket(drawDto4.getTicket());
                           result.setPlace("first");
                           result.setTotelPrice((double) (n.getCount() * 700));
                           resultRepository.save(result);
                       }
                   }

                }
                else if(n.getType().equals("super")){

                    if (n.getCoupenNumber().contains(first)) {

                        result = new Result();
                        result.setTicketDetails(n);
                        result.setTicket(drawDto4.getTicket());
                        result.setPlace("first");
                        result.setTotelPrice((double) (n.getCount() * prize.getFirsst()));
                        resultRepository.save(result);
                    } else if (n.getCoupenNumber().contains(second)) {

                        result = new Result();
                        result.setTicketDetails(n);
                        result.setTicket(drawDto4.getTicket());
                        result.setPlace("second");
                        result.setTotelPrice((double) (n.getCount() * prize.getSecond()));
                        resultRepository.save(result);

                    } else if (n.getCoupenNumber().contains(third)) {

                        result = new Result();
                        result.setTicketDetails(n);
                        result.setTicket(drawDto4.getTicket());
                        result.setPlace("third");
                        result.setTotelPrice((double) (n.getCount() * prize.getThird()));
                        resultRepository.save(result);
                    } else if (n.getCoupenNumber().contains(fourth)) {

                        result = new Result();
                        result.setTicketDetails(n);
                        result.setTicket(drawDto4.getTicket());
                        result.setPlace("fourth");
                        result.setTotelPrice((double) (n.getCount() * prize.getFourth()));
                        resultRepository.save(result);
                    } else if (n.getCoupenNumber().contains(fifth)) {

                        result = new Result();
                        result.setTicketDetails(n);
                        result.setTicket(drawDto4.getTicket());
                        result.setPlace("fifth");
                        result.setTotelPrice((double) (n.getCount() * prize.getFifth()));
                        resultRepository.save(result);
                    } else {
                        for (String s : complimentary) {

                            if (n.getCoupenNumber().contains(s)) {
                                result = new Result();
                                result.setTicketDetails(n);
                                result.setTicket(drawDto4.getTicket());
                                result.setPlace("complimentary");
                                result.setTotelPrice((double) (n.getCount() * prize.getComplimentary()));
                                resultRepository.save(result);
                            }
                        }

                    }
                } else if (n.getType().equals("box")) {
                    List<String> frst = new ArrayList<>();

                    generatePermutations(first.toCharArray(), 0, frst);
                    Set<String> set = new HashSet<>(frst);
                    for (String n1 : set) {
                        if (n.getCoupenNumber().contains(n1)) {
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getFirsst()));
                            resultRepository.save(result);
                        }
                    }
                    List<String> scnd = new ArrayList<>();
                    generatePermutations(second.toCharArray(), 0, scnd);
                    Set<String> set2 = new HashSet<>(scnd);
                    for (String n1 : set2) {
                        if (n.getCoupenNumber().contains(n1)) {
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("second");
                            result.setTotelPrice((double) (n.getCount() * prize.getSecond()));
                            resultRepository.save(result);
                        }
                    }
                    List<String> thrd = new ArrayList<>();
                    generatePermutations(third.toCharArray(), 0, thrd);
                    Set<String> set3 = new HashSet<>(thrd);
                    for (String n1 : set3) {
                        if (n.getCoupenNumber().contains(n1)) {
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("third");
                            result.setTotelPrice((double) (n.getCount() * prize.getThird()));
                            resultRepository.save(result);
                        }
                    }
                    List<String> frt = new ArrayList<>();
                    generatePermutations(fourth.toCharArray(), 0, frt);
                    Set<String> set4 = new HashSet<>(frt);
                    for (String n1 : set4) {
                        if (n.getCoupenNumber().contains(n1)) {
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("fourth");
                            result.setTotelPrice((double) (n.getCount() * prize.getFourth()));
                            resultRepository.save(result);
                        }
                    }
                    List<String> fvt = new ArrayList<>();
                    generatePermutations(fifth.toCharArray(), 0, fvt);
                    Set<String> set5 = new HashSet<>(fvt);
                    for (String n1 : set5) {
                        if (n.getCoupenNumber().contains(n1)) {
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("fifth");
                            result.setTotelPrice((double) (n.getCount() * prize.getFifth()));
                            resultRepository.save(result);
                        }
                    }
                    for (String s : complimentary) {
                        List<String> cmp = new ArrayList<>();
                        generatePermutations(s.toCharArray(), 0, cmp);
                        Set<String> set6 = new HashSet<>(cmp);
                        for (String n1 : set6) {
                            if (n.getCoupenNumber().contains(n1)) {
                                result = new Result();
                                result.setTicketDetails(n);
                                result.setTicket(drawDto4.getTicket());
                                result.setPlace("complimentary");
                                result.setTotelPrice((double) (n.getCount() * prize.getComplimentary()));
                                resultRepository.save(result);
                            }
                        }

                    }


                } else if (n.getType().equals("block")) {
                    if(n.getCoupenNumber().contains("abc") || n.getCoupenNumber().contains("Abc")||n.getCoupenNumber().contains("ABC")){
                        String a1=removeNonDigits(n.getCoupenNumber());
//                        if(a1.length()==1){
//                            a1=a1+a1+a1;
//                        }
                        String f1=first;
                        if(first.contains(a1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }

                    }
                  else  if(n.getCoupenNumber().contains("ab") || n.getCoupenNumber().contains("Ab")||n.getCoupenNumber().contains("AB")){

                        String a1=removeNonDigits(n.getCoupenNumber());
//                        if(a1.length()==1){
//                            a1=a1+a1;
//                        }
                        String f1=findFirstTwoDigits(first);
                        if(f1.contains(a1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }


                    } else if (n.getCoupenNumber().contains("bc") || n.getCoupenNumber().contains("Bc") || n.getCoupenNumber().contains("BC")) {
                        String a1=removeNonDigits(n.getCoupenNumber());
//                        if(a1.length()==1){
//                            a1=a1+a1;
//                        }
                        String f1=findLastTwoNumber(first);
                        if(f1.contains(a1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }



                    }
                    else if(n.getCoupenNumber().contains("ac")|| n.getCoupenNumber().contains("AC")|| n.getCoupenNumber().contains("Ac")){
                        String a1=removeNonDigits(n.getCoupenNumber());
//                        if(a1.length()==1){
//                            a1=a1+a1;
//                        }
                        String f1=findFirstAndLastDigits(first);
                        if(f1.contains(a1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }

                    }


//                    conatin A

                   else if(n.getCoupenNumber().contains("a")||n.getCoupenNumber().contains("A")){
                        String f1=findFirstDigit(first);
                        String a1= removeNonDigits(n.getCoupenNumber());
                        if(a1.contains(f1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }

                    }
                    else if(n.getCoupenNumber().contains("b")||n.getCoupenNumber().contains("B")){
                        String f1=findSecondDigit(first);
                        String a1= removeNonDigits(n.getCoupenNumber());
                        if(a1.contains(f1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }

                    }
                    else if(n.getCoupenNumber().contains("c")||n.getCoupenNumber().contains("C")){
                        String f1=findThirdDigit(first);
                        String a1= removeNonDigits(n.getCoupenNumber());
                        if(a1.contains(f1)){
                            result = new Result();
                            result.setTicketDetails(n);
                            result.setTicket(drawDto4.getTicket());
                            result.setPlace("first");
                            result.setTotelPrice((double) (n.getCount() * prize.getBlockPrize()));
                            resultRepository.save(result);
                        }

                    }

                }
            }
        }




    }
    private static String findFirstAndLastDigits(String input) {
        // Ensure input is a two-digit string
        char firstDigit = input.charAt(0);
        char lastDigit = input.charAt(2);
        return String.valueOf(firstDigit) + String.valueOf(lastDigit);

    }
    private static String removeNonDigits(String input) {
        // Use regular expression to remove all non-digit characters
        return input.replaceAll("[^0-9]", "");
    }

    private static String findFirstTwoDigits(String input) {
        // Use regular expression to find the first two digits
        return input.substring(0,2);
    }
    private static String findLastTwoNumber(String input){

        return input.substring(1);
    }

    public static String findFirstLetter(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                return Character.toString(c);
            }
        }
        return ""; // Return empty string if no letter found
    }

    public static String findFirstDigit(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                return Character.toString(c);
            }
        }
        return " "; // Return null character if no digit is found
    }
    public static String findSecondDigit(String input) {
        int digitCount = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
                if (digitCount == 2) {
                    return Character.toString(c);
                }
            }
        }
        return " "; // Return null character if the second digit is not found
    }
    public static String findThirdDigit(String input) {
        int digitCount = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
                if (digitCount == 3) {
                    return Character.toString(c);
                }
            }
        }
        return " "; // Return null character if the second digit is not found
    }


    private void generatePermutations(char[] chars, int index, List<String> result) {
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

    @Override
    public List<Result> findResultByTicketandDate(Long id, LocalDate date, String place) {
        return resultRepository.findResultByTicketandDate(id, date, place);
    }

    @Override
    public List<Result> findDailyResult(Long id, LocalDate date) {
        return resultRepository.findByResult(id,date);
    }

    @Override
    public List<LimitExceedData> findLimitExceedData(Long id, LocalDate date) {
        return ticketLimitRepository.findByLmitExceedData(id,date);
    }

    @Override
    public void deleteLimit(Long[] id) {
        for (Long cid:id){
            LimitExceedData limit=ticketLimitRepository.getReferenceById(cid);
            limit.setActivated(false);
            ticketLimitRepository.save(limit);

        }
    }

    @Override
    public List<DailyResult> dailyResultReport(Long id, LocalDate date) {
       List<Object[]> objects=resultRepository.findDailyResultReport(id, date);
       List<DailyResult> dailyResults=new ArrayList<>();
       for (Object[] row:objects){
           String prize=(String) row[0];
           Double prizeAmount=(Double) row[1];
           dailyResults.add(new DailyResult(prize,prizeAmount));
       }

        return dailyResults;
    }

    @Override
    public Double totelPrizeAmount(Long id, LocalDate date) {
        return resultRepository.totelPrizeAmount(id,date);
    }
}
