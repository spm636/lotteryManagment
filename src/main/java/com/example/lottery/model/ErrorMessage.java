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

public class ErrorMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "error_id")
    private Long id;

    private String coupen;
    private boolean isAcivated=true;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_id",referencedColumnName = "ticket_id")
    private Ticket ticket;
}
