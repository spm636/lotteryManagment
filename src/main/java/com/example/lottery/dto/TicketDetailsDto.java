package com.example.lottery.dto;

import com.example.lottery.model.Ticket;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketDetailsDto {
    private Long id;
    private String enterDetails;
    private String name;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate date;
    private Ticket ticket;
    private Integer limit;

}
