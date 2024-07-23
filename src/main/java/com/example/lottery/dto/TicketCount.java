package com.example.lottery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketCount {
    private String coupenType;
    private int totelCount;

    public TicketCount(String coupenType, int totelCount) {
        this.coupenType = coupenType;
        this.totelCount = totelCount;
    }
}
