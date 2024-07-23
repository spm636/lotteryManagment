package com.example.lottery.controller;

import com.example.lottery.dto.DrawDto4;
import com.example.lottery.model.Result;
import com.example.lottery.model.Ticket;
import com.example.lottery.service.ResultService;
import com.example.lottery.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Controller
public class DrawController {
    @Autowired
    TicketService ticketService;

    @Autowired
    ResultService resultService;


    @GetMapping("/drawPort")
    public String showDrawPort(Model model){
        DrawDto4 drawDto4=new DrawDto4();
        model.addAttribute("draw",drawDto4);
        List<Ticket> tickets=ticketService.findAllTicket();
        model.addAttribute("ticket",tickets);
        LocalDate today=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        model.addAttribute("formattedDate", formattedDate);
        return "draw-ticket";
    }
    @PostMapping("/drawTicket")
    public String showDrawTicket(@ModelAttribute("draw")DrawDto4 drawDto4,Model model){
        resultService.saveResult(drawDto4);
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
        return "draw-ticket";
    }

}
