package com.example.lottery.service;

import com.example.lottery.dto.PrizeAmountDto;
import com.example.lottery.model.PrizeAmount;

public interface PrizeAmountService {

    void updatePrize(Long id, PrizeAmountDto prizeAmountDto);
    PrizeAmount findByid(Long id);
}
