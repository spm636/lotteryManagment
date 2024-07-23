package com.example.lottery.repository;

import com.example.lottery.model.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage,Long> {
    @Query("select e from ErrorMessage e where e.isAcivated=true and e.ticket.id=?1 and e.coupen IS NOT NULL and e.coupen <> ''")
    List<ErrorMessage> findByAcivated(Long id);
}
