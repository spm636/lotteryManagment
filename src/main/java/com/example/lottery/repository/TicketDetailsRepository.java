package com.example.lottery.repository;

import com.example.lottery.model.TicketDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetails,Long> {

    @Query("SELECT t.type AS coupenType,SUM(t.count) AS totelCount from TicketDetails t where t.isActivated=true and t.date=?1 and t.ticket.id=?2 GROUP BY t.type ")
    List<Object[]> ticketCount(LocalDate date,long id);

    @Query("select t.type AS coupenType,SUM (t.count) AS totelCount,SUM (t.rate) AS amount from TicketDetails  t where t.date=?1 and t.ticket.id=?2 and t.isActivated=true GROUP BY t.type")
   List<Object[]> dailyTicketDetails(LocalDate date,Long id);
    @Query("SELECT sum (t.rate) AS totelCopenRate from TicketDetails t WHERE t.date=?1 AND t.ticket.id=?2 AND t.isActivated=true ")
    public Double findTotelCoupenAmount(LocalDate date,Long id);
    @Query("SELECT td from TicketDetails td join Ticket t on td.ticket.id=t.id where td.ticket.id=?1 and td.date=?2 and td.isActivated=true order by td.id desc ")
    List<TicketDetails> findTicketDetailsByDateAndTicket(Long ticketId,LocalDate date);
    @Query("SELECT t FROM TicketDetails t WHERE t.ticket.id = ?1 AND t.date = ?2 AND t.isActivated = true AND t.coupenNumber LIKE ?3%")
    List<TicketDetails> findTicketDetailsByKeyword(Long id, LocalDate date, String keyword);


}
