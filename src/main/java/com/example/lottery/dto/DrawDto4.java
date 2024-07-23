package com.example.lottery.dto;

import com.example.lottery.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DrawDto4 {
    private String result;
    private Ticket ticket;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate date;

}
