package com.example.lottery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AdminConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
 @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
     http.authorizeHttpRequests(configurer->
             configurer
                     .anyRequest().authenticated()
     )
             .formLogin(form->
                     form
                             .loginPage("/login")
                             .loginProcessingUrl("/authenticateTheUser")
                             .defaultSuccessUrl("/")


                             .permitAll()
             )
             .logout(LogoutConfigurer->
                     LogoutConfigurer
                             .logoutSuccessUrl("/login")
             );
     return http.build();
 }
}
