package com.example.lottery.controller;

import com.example.lottery.config.CustomeUser;
import com.example.lottery.dto.*;
import com.example.lottery.model.*;
import com.example.lottery.service.*;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Getter
    @Autowired
    ResultService resultService;
    @Autowired
    TicketService ticketService;
    @Autowired
    TicketDetailsService ticketDetailsService;
    @Autowired
    PrizeAmountService prizeAmountService;
    @Autowired
    TicketRateService ticketRateService;
    @Autowired
    AdminService adminService;

    @GetMapping("/resultDowload")
    public String showResultDowload(Model model){

        DrawDto4 drawDto4=new DrawDto4();
        model.addAttribute("res",drawDto4);
        List<Ticket> tickets=ticketService.findAllTicket();
        model.addAttribute("ticket",tickets);
        LocalDate today=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        model.addAttribute("formattedDate", formattedDate);
        return "result-dowload";
    }
    @GetMapping("/dowloadPdf")
    public String generatePdf(@ModelAttribute("res")DrawDto4 drawDto4, HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        Long id=drawDto4.getTicket().getId();
        LocalDate date=drawDto4.getDate();
        List<Result> resulttList =resultService.findDailyResult(id,date);
        //List<DailyTicketReport> resulttList=ticketDetailsService.dailyticketReport(date,id);
        PdfGenerator generator = new PdfGenerator();
        //PdfReport generator=new PdfReport();
        generator.setResults(resulttList);
        generator.generate(response);
        return "redirect:/resultDowload";
    }
    @GetMapping("/dowloadReport")
    public String generatePdfReport(@ModelAttribute("res")DrawDto4 drawDto4, HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        Long id=drawDto4.getTicket().getId();
        LocalDate date=drawDto4.getDate();

        List<DailyTicketReport> resulttList=ticketDetailsService.dailyticketReport(date,id);
        List<Result> results=resultService.findDailyResult(id,date);
        PdfReport generator=new PdfReport();
        generator.setReports(resulttList);
        generator.setResults(results);
        List<DailyResult> dailyResults=resultService.dailyResultReport(id,date);
        Double totelCoupenAmount=ticketDetailsService.findTotelCoupenAmount(date,id);
        Double totelPrize=resultService.totelPrizeAmount(id,date);
        Double profit=totelCoupenAmount-totelPrize;
        generator.setDailyResults(dailyResults);
        generator.setTotelCoupenAmount(totelCoupenAmount);
        generator.setTotelPrize(totelPrize);
        generator.setProfit(profit);
        generator.generate(response);
        return "redirect:/report";
    }

    @GetMapping("/editPrize")
    public String showEditPrize(Model model){
        Long id=1L;
        PrizeAmount prizeAmountDto=prizeAmountService.findByid(id);
        model.addAttribute("prize",prizeAmountDto);

        TicketRate coupenPrizeDto=ticketRateService.findByid(id);
        model.addAttribute("coupen",coupenPrizeDto);
        return "edit-Prize";
    }
    @GetMapping("/editCoupenPrize")
    public String saveCoupenPrize(@ModelAttribute("coupen")CoupenPrizeDto coupenPrizeDto){
        ticketRateService.update(coupenPrizeDto);
        return "redirect:/editPrize";
    }

    @GetMapping("/editPrizeMoney")
    public String saveEditPrize(@ModelAttribute("prize") PrizeAmountDto prizeAmountDto){
        Long id= 1L;
        prizeAmountService.updatePrize(id,prizeAmountDto);
        return "redirect:/editPrize";
    }
    @GetMapping("/report")
    public String showReport(Model model){
        DrawDto4 drawDto4=new DrawDto4();
        model.addAttribute("res",drawDto4);
        List<Ticket> tickets=ticketService.findAllTicket();
        model.addAttribute("ticket",tickets);
        LocalDate today=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        model.addAttribute("formattedDate", formattedDate);

        return "report";
    }
    @GetMapping("/changePassword")
    public String ShowPassword(Principal principal, Model model, Authentication authentication){
      String username=principal.getName();
        CustomeUser customeUser= (CustomeUser) authentication.getPrincipal();
        Long id=customeUser.getId();

        Admin adminDto=adminService.findById(id);
        model.addAttribute("admin",adminDto);
        return "change-password";
    }
    @GetMapping("/saveChangePassword")
    public String showChngePasseord(@ModelAttribute("admin")AdminDto adminDto,Authentication authentication){
        CustomeUser customeUser= (CustomeUser) authentication.getPrincipal();
        Long id=customeUser.getId();
        adminService.update(id,adminDto);
        return "redirect:/";
    }
    @GetMapping("/ShowResult")
    public String showResult(Model model){
        DrawDto4 drawDto4=new DrawDto4();
        model.addAttribute("draw",drawDto4);
        List<Ticket> tickets=ticketService.findAllTicket();
        model.addAttribute("ticket",tickets);
        LocalDate today=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        model.addAttribute("formattedDate", formattedDate);
        return "show-result";
    }
    @GetMapping("/showAllResult")
    public String showAllResult(@ModelAttribute("draw")DrawDto4 drawDto4,Model model){
        Long id=drawDto4.getTicket().getId();
        LocalDate date=drawDto4.getDate();
        String res1="first";
        String res2="second";
        String res3="third";
        String res4="fourth";
        String res5="fifth";
        String res6="complimentary";
        List<Result> result1=resultService.findResultByTicketandDate(id,date,res1);
        model.addAttribute("res1",result1);
        List<Result> result2=resultService.findResultByTicketandDate(id,date,res2);
        model.addAttribute("res2",result2);
        List<Result> result3=resultService.findResultByTicketandDate(id,date,res3);
        model.addAttribute("res3",result3);
        List<Result> result4=resultService.findResultByTicketandDate(id,date,res4);
        model.addAttribute("res4",result4);
        List<Result> result5=resultService.findResultByTicketandDate(id,date,res5);
        model.addAttribute("res5",result5);
        List<Result> result6=resultService.findResultByTicketandDate(id,date,res6);
        model.addAttribute("res6",result6);
        return "show-result";
    }
}
