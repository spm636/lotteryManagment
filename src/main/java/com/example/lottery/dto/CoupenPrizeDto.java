package com.example.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoupenPrizeDto {
    private Long id;
    private Double super1;
    private Double box;
    private Double block;
}
