package com.example.lottery.service;

import com.example.lottery.dto.DailyTicketReport;
import com.example.lottery.dto.Execepted;
import com.example.lottery.dto.TicketCount;
import com.example.lottery.dto.TicketDetailsDto;
import com.example.lottery.model.ErrorMessage;
import com.example.lottery.model.TicketDetails;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface TicketDetailsService {
    void saveTicketDetails(TicketDetailsDto ticketDetailsDto);
    List<TicketDetails> findAllticketDetails();
    void delete(Long id);
   public List<TicketCount> ticketCount(LocalDate date,Long id);

   public List<DailyTicketReport> dailyticketReport(LocalDate date,Long id);
   public void deleteSelected(Long[] id);
   List<TicketDetails> findByDateAndTicket(Long id, LocalDate date);
   List<String> ErrorData();
   List<ErrorMessage> findActiveError(Long id);
   public void deleteError(Long[] id);
    List<TicketDetails> findTicketDetailsBykeyWord(Long id,LocalDate date,String keyword);
    public Double findTotelCoupenAmount(LocalDate date,Long id);

}
