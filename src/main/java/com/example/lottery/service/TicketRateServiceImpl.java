package com.example.lottery.service;

import com.example.lottery.dto.CoupenPrizeDto;
import com.example.lottery.model.TicketRate;
import com.example.lottery.repository.TicketRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketRateServiceImpl implements TicketRateService
{
    @Autowired
    TicketRateRepository ticketRateRepository;
    @Override
    public TicketRate findByid(Long id) {
        return ticketRateRepository.getReferenceById(id);
    }

    @Override
    public void update(CoupenPrizeDto coupenPrizeDto) {
        Long id=1L;
        TicketRate ticketRate=ticketRateRepository.getReferenceById(id);
        ticketRate.setSuper1(coupenPrizeDto.getSuper1());
        ticketRate.setBox(coupenPrizeDto.getBox());
        ticketRate.setBlock(coupenPrizeDto.getBlock());
        ticketRateRepository.save(ticketRate);
    }
}
