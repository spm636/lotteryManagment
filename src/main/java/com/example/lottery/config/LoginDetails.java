package com.example.lottery.config;

import com.example.lottery.model.Admin;
import com.example.lottery.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginDetails implements UserDetailsService {
@Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName;
        List<Admin> adminDetails=adminRepository.findByUsername(username);
        userName=adminDetails.get(0).getUsername();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(adminDetails.get(0).getRole()));

        return new CustomeUser(username,adminDetails.get(0).getPassword(),authorities,adminDetails.get(0).getEmail(),adminDetails.get(0).getUsername(),adminDetails.get(0).getId());
    }
}
