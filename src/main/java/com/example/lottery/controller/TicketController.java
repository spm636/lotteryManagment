package com.example.lottery.controller;

import com.example.lottery.dto.DrawDto4;
import com.example.lottery.dto.TicketCount;
import com.example.lottery.dto.TicketDetailsDto;
import com.example.lottery.dto.TicketDto;
import com.example.lottery.model.ErrorMessage;
import com.example.lottery.model.LimitExceedData;
import com.example.lottery.model.Ticket;
import com.example.lottery.model.TicketDetails;
import com.example.lottery.service.ResultService;
import com.example.lottery.service.TicketDetailsService;
import com.example.lottery.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {

    private final TicketDetailsService ticketDetailsService;
    private final TicketService ticketService;
    @Autowired
    ResultService resultService;
  @Autowired
    public TicketController(TicketDetailsService ticketDetailsService, TicketService ticketService) {
        this.ticketDetailsService = ticketDetailsService;
      this.ticketService = ticketService;
  }

    @GetMapping("/ticket")
    public String showTicket(@RequestParam("ticketId")Long id, Model model){


        TicketDetailsDto ticketDetailsDto=new TicketDetailsDto();
        model.addAttribute("ticketDetails",ticketDetailsDto);

        LocalDate today=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        List<TicketDetails> ticketDetails=ticketDetailsService.findByDateAndTicket(id, LocalDate.parse(formattedDate));
        model.addAttribute("ticketDetail",ticketDetails);
        List<TicketCount> ticketCounts=ticketDetailsService.ticketCount(LocalDate.parse(formattedDate),id);
        model.addAttribute("ticketCount",ticketCounts);

       List<ErrorMessage> error=ticketDetailsService.findActiveError(id);
       model.addAttribute("error",error);

       List<LimitExceedData> limitExceedData=resultService.findLimitExceedData(id, LocalDate.parse(formattedDate));
        model.addAttribute("limit",limitExceedData);

        model.addAttribute("formattedDate", formattedDate);
        Ticket findById=ticketService.findById(id);
        model.addAttribute("ticketName",findById);

        Ticket ticket=ticketService.findById(id);
        model.addAttribute("ticketId",ticket);
        return "DisplayTicketDetails";
    }
    @GetMapping("/search")
    public String showSearch(Model model,@RequestParam("keyword")String keyword,@RequestParam("tid")Long tid){
        TicketDetailsDto ticketDetailsDto=new TicketDetailsDto();
        model.addAttribute("ticketDetails",ticketDetailsDto);

        LocalDate today=LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        List<TicketDetails> ticketDetails= ticketDetailsService.findTicketDetailsBykeyWord(tid, LocalDate.parse(formattedDate),keyword);
      model.addAttribute("ticketDetail",ticketDetails);
        List<TicketCount> ticketCounts=ticketDetailsService.ticketCount(LocalDate.parse(formattedDate),tid);
        model.addAttribute("ticketCount",ticketCounts);

        List<ErrorMessage> error=ticketDetailsService.findActiveError(tid);
        model.addAttribute("error",error);

        List<LimitExceedData> limitExceedData=resultService.findLimitExceedData(tid, LocalDate.parse(formattedDate));
        model.addAttribute("limit",limitExceedData);

        model.addAttribute("formattedDate", formattedDate);
        Ticket findById=ticketService.findById(tid);
        model.addAttribute("ticketName",findById);
        Ticket ticket=ticketService.findById(tid);
        model.addAttribute("ticketId",ticket);
        return "DisplayTicketDetails";
    }


    @PostMapping("/addTicket")
    public String showAddTicket(@ModelAttribute("ticketDetails")TicketDetailsDto ticketDetailsDto){

      ticketDetailsService.saveTicketDetails(ticketDetailsDto);
      Long id=ticketDetailsDto.getTicket().getId();
     //   System.out.println(id);
       // System.out.println(ticketDetailsDto);
        return "redirect:/ticket?ticketId="+id;
    }
    @GetMapping("showAll")
    public String showAll(@RequestParam("ticketId")Long id){
        return "redirect:/ticket?ticketId="+id;
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("ticketId") Long theID){
        ticketDetailsService.delete(theID);

        return "redirect:/ticket";
    }

    @GetMapping("/")
    public String showHomePage(Model model){
        List<Ticket> tickets=ticketService.findAllTicket();
        model.addAttribute("ticket",tickets);
      return "home";
    }
    @GetMapping("/login")
    public String showLogin(){
      return "login";
    }
    @GetMapping("/addTicket")
    public String showAddTicke(Model model){
        TicketDto ticketDto=new TicketDto();
        model.addAttribute("ticket",ticketDto);
      return "add-ticket";
    }
    @PostMapping("/saveTicket")
    public String saveTicketDetails(@ModelAttribute("ticket")TicketDto ticketDto){
      ticketService.saveTicket(ticketDto);
      return "redirect:/";
    }
    @GetMapping("/deleteTicket")
    public String deleteTicket(@RequestParam("ticketId")Long id){
      ticketService.deleteTicket(id);
      return "redirect:/";
    }
   @GetMapping("/deleteSelectes")
    public String deleteSelected(@RequestParam(name="cid",required = false)Long[] id,@RequestParam("tid")Long tid){
       if(id==null || id.length==0){
           System.out.println("tid is"+id);
           return "redirect:/ticket?ticketId="+tid;
       }
        ticketDetailsService.deleteSelected(id);
       return "redirect:/ticket?ticketId="+tid;
   }
   @GetMapping("/deleteSelectesError")
    public String deleteSelectedError(@RequestParam(name = "selectedItems", required = false) Long[] id,@RequestParam("tid")Long tid){
      if(id==null || id.length==0){
          System.out.println("tid is"+id);
          return "redirect:/ticket?ticketId="+tid;
      }
        ticketDetailsService.deleteError(id);
       return "redirect:/ticket?ticketId="+tid;
   }
   @GetMapping("/deleteLimit")
   public String deleteSelecteLimit(@RequestParam(name= "cid",required = false)Long[] id,@RequestParam("tid")Long tid){
       if(id==null || id.length==0){
           System.out.println("tid is"+id);
           return "redirect:/ticket?ticketId="+tid;
       }
      resultService.deleteLimit(id);
       return "redirect:/ticket?ticketId="+tid;
   }


}
