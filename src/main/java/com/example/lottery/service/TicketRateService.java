package com.example.lottery.service;

import com.example.lottery.dto.CoupenPrizeDto;
import com.example.lottery.model.TicketRate;

public interface TicketRateService {

    TicketRate findByid(Long id);
    void update(CoupenPrizeDto coupenPrizeDto);
}
