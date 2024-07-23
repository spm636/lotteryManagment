package com.example.lottery.repository;

import com.example.lottery.model.TicketRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRateRepository extends JpaRepository<TicketRate,Long> {

}
