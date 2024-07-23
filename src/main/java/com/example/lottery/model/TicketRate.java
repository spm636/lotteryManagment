package com.example.lottery.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TicketRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double super1;
    private Double box;
    private Double block;

}
