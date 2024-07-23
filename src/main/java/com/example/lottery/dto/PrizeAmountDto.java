package com.example.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrizeAmountDto {
    private Long id;
    private Double firsst;
    private Double second;
    private Double third;
    private Double fourth;
    private Double fifth;
    private Double complimentary;
    private Double blockPrize;
}
