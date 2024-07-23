package com.example.lottery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DailyResult {
    private String prize;
    private Double prizeAmout;

    public DailyResult(String prize, Double prizeAmout) {
        this.prize = prize;
        this.prizeAmout = prizeAmout;
    }
}
