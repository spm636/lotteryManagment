package com.example.lottery.repository;

import com.example.lottery.model.LimitExceedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketLimitRepository extends JpaRepository<LimitExceedData,Long> {
    @Query("select l from LimitExceedData l where l.ticket.id=?1 and l.date=?2 and l.isActivated=true ")
    List<LimitExceedData> findByLmitExceedData(Long id, LocalDate date);
}
