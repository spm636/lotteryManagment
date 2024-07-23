package com.example.lottery.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LimitExceedData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ticket_limit_id")
    private Long id;

    private String coupenNumber;
    private String name;
    private String type;
    private int count;
    private Double rate;

    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate date;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id",referencedColumnName = "ticket_id")
    private Ticket ticket;
    private boolean isActivated=true;
}
