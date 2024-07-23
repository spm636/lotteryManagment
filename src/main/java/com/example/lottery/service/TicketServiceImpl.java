package com.example.lottery.service;

import com.example.lottery.dto.TicketDto;
import com.example.lottery.model.Ticket;
import com.example.lottery.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService{
    @Autowired
    TicketRepository ticketRepository;

    @Override
    public void saveTicket(TicketDto ticketDto) {
        Ticket ticket=new Ticket();
        ticket.setTicket(ticketDto.getTicket());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setActivated(true);
        ticketRepository.save(ticket);

    }

    @Override
    public List<Ticket> findAllTicket() {

        return ticketRepository.findByActivated();
    }

    @Override
    public void deleteTicket(long id) {

        Ticket ticket=ticketRepository.getReferenceById(id);
        ticket.setActivated(false);
        ticketRepository.save(ticket);
    }

    @Override
    public Ticket findById(Long id) {
        return ticketRepository.getReferenceById(id);
    }
}
