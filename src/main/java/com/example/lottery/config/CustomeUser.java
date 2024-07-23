package com.example.lottery.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomeUser extends User {
   private String name;
   private String email;
   private Long id;

    public CustomeUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String name, String email, Long id) {
        super(username, password, authorities);
        this.name = name;
        this.email = email;
        this.id = id;
    }
}
