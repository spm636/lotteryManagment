package com.example.lottery.service;

import com.example.lottery.dto.AdminDto;
import com.example.lottery.model.Admin;
import com.example.lottery.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin findById(Long id) {
        return adminRepository.getReferenceById(id);
    }

    @Override
    public void update(Long id, AdminDto adminDto) {
        Admin admin= (Admin) adminRepository.getReferenceById(id);
        admin.setPassword(adminDto.getPassword());
        adminRepository.save(admin);

    }
}
