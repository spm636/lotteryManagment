package com.example.lottery.repository;

import com.example.lottery.model.PrizeAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeamountTepository extends JpaRepository<PrizeAmount,Long> {

}
