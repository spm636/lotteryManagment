package com.example.lottery.dto;

import lombok.*;


@NoArgsConstructor

@Data
public class DailyTicketReport {
    private String coupenType;
    private int totelCount;
    private Double amount;

    public DailyTicketReport(String coupenType, int totelCount, Double amount) {
        this.coupenType = coupenType;
        this.totelCount = totelCount;
        this.amount = amount;
    }
}
