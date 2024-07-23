package com.example.lottery.repository;

import com.example.lottery.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    List<Admin> findByUsername(String username);
}
