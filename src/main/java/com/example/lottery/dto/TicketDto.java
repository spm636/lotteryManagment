package com.example.lottery.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TicketDto {
    private Long id;
    private String ticket;
    private String Description;

}
