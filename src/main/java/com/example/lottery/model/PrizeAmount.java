package com.example.lottery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class PrizeAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double firsst;
    private Double second;
    private Double third;
    private Double fourth;
    private Double fifth;
    private Double complimentary;
    private Double blockPrize;
}
