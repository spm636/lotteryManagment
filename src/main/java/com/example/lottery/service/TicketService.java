package com.example.lottery.service;

import com.example.lottery.dto.TicketDto;
import com.example.lottery.model.Ticket;

import java.util.List;

public interface TicketService {
    void saveTicket(TicketDto ticketDto);
    List<Ticket> findAllTicket();

    void deleteTicket(long id);
    Ticket findById(Long id);
}
