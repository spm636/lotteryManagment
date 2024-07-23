package com.example.lottery.service;

import com.example.lottery.dto.AdminDto;
import com.example.lottery.model.Admin;

public interface AdminService {
    public Admin findById(Long id);
    void update(Long id, AdminDto adminDto);
}
