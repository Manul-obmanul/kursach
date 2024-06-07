package com.example.kursach.configuration;

import com.example.kursach.entity.UserAuthority;
import com.example.kursach.service.impl.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private MyUserDetailsService service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry
                                .requestMatchers("/registration", "/products/{id}", "/products/getall", "/users/**").permitAll()
                                .requestMatchers("/purchases/*", "/users/getInfo/**").hasAuthority(UserAuthority.PLACE_ORDERS.getAuthority())
                                .requestMatchers(HttpMethod.DELETE, "/products/**", "/purchases/**").hasAuthority(UserAuthority.MANAGE_ORDERS.getAuthority())
                                .requestMatchers(HttpMethod.PUT, "/products/**", "/purchases/**").hasAuthority(UserAuthority.MANAGE_ORDERS.getAuthority())
                                .requestMatchers(HttpMethod.POST, "/products/**", "/purchases/**").hasAuthority(UserAuthority.MANAGE_ORDERS.getAuthority())
                                .anyRequest().hasAuthority(UserAuthority.FULL.getAuthority()))
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        return new MyUserDetailsService();
    }
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

