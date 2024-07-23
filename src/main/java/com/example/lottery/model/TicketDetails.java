package com.example.lottery.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="ticket_details")
public class TicketDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ticket_details_id")
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
