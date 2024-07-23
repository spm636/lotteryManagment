package com.example.lottery.service;

import com.example.lottery.dto.PrizeAmountDto;
import com.example.lottery.model.PrizeAmount;
import com.example.lottery.repository.PrizeamountTepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrizeAmountServiceImpl implements PrizeAmountService {
    @Autowired
    PrizeamountTepository prizeamountTepository;

    @Override
    public void updatePrize( Long id,PrizeAmountDto prizeAmountDto) {
        PrizeAmount prizeAmount=prizeamountTepository.getReferenceById(id);
        prizeAmount.setFirsst(prizeAmountDto.getFirsst());
        prizeAmount.setSecond(prizeAmountDto.getSecond());
        prizeAmount.setThird(prizeAmountDto.getThird());
        prizeAmount.setFourth(prizeAmountDto.getFourth());
        prizeAmount.setFifth(prizeAmountDto.getFifth());
        prizeAmount.setComplimentary(prizeAmountDto.getComplimentary());
        prizeAmount.setBlockPrize(prizeAmountDto.getBlockPrize());
        prizeamountTepository.save(prizeAmount);
    }

    @Override
    public PrizeAmount findByid(Long id) {
        return prizeamountTepository.getReferenceById(id);
    }
}
