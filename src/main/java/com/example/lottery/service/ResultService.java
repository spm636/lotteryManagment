package com.example.lottery.service;

import com.example.lottery.dto.DailyResult;
import com.example.lottery.dto.DrawDto4;
import com.example.lottery.model.LimitExceedData;
import com.example.lottery.model.Result;

import java.time.LocalDate;
import java.util.List;

public interface ResultService {

    void saveResult(DrawDto4 drawDto4);
    List<Result> findResultByTicketandDate(Long id, LocalDate date, String place);
    List<Result> findDailyResult(Long id,LocalDate date);

    List<LimitExceedData> findLimitExceedData(Long id,LocalDate date);
    public void deleteLimit(Long[] id);

    List<DailyResult> dailyResultReport(Long id,LocalDate date);
    Double totelPrizeAmount(Long id,LocalDate date);
}
