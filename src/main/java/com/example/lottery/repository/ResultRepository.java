package com.example.lottery.repository;

import com.example.lottery.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result,Long> {

    @Query("select r from Result r where r.ticket.id=?1 and r.ticketDetails.date=?2 and r.place=?3")
    List<Result> findResultByTicketandDate(Long id, LocalDate date,String place);

    @Query("select r from Result r where r.ticket.id=?1 and r.ticketDetails.date=?2 ORDER BY CASE r.place WHEN 'first' THEN 1 WHEN 'second' THEN 2 WHEN 'third' THEN 3 WHEN 'fourth' THEN 4 WHEN 'fifth' THEN 5 ELSE 6 END")
    List<Result> findByResult(Long id,LocalDate date);

    @Query("select r.place AS prize,sum (r.totelPrice) AS prizeAmount from Result r where r.ticket.id=?1 and r.ticketDetails.date=?2 group by prize ORDER BY CASE r.place WHEN 'first' THEN 1 WHEN 'second' THEN 2 WHEN 'third' THEN 3 WHEN 'fourth' THEN 4 WHEN 'fifth' THEN 5 ELSE 6 END")
    List<Object[]> findDailyResultReport(Long id,LocalDate date);

    @Query("select sum (r.totelPrice) from Result r where r.ticket.id=?1 and r.ticketDetails.date=?2")
    public Double totelPrizeAmount(Long id,LocalDate date);
}
