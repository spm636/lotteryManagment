package com.example.lottery.repository;

import com.example.lottery.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @Query("select t from Ticket t where t.isActivated=true ")
    List<Ticket> findByActivated();
}
