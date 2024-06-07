package com.example.kursach.controller;

import com.example.kursach.entity.User;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    public UserServiceImpl userService;
    @Autowired
    public UserRepository userRepository;
    @PutMapping("/{id}")
    public String updateUser(@RequestParam("id")  Long id,
                             @RequestParam("username") String username,
                             @RequestParam("email") String email,
                             @RequestParam("phone") Long phone,
                             @RequestParam("password") String password,
                             @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return userService.updateUser(id, username, email, phone, password, loadName);
    }

    @DeleteMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username, @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return userService.deleteUser(username, loadName);
    }

    @GetMapping("/getinfo/{username}")
    public ResponseEntity<?> getInfo(@PathVariable("username") String username, @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return userService.getInfo(username, loadName);
    }
    //TODO ТЕСТОВАЯ ФИГНЯ, ПОЗЖЕ УДАЛИТЬ
    @GetMapping("/user")
    public String getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // получить имя пользователя
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); // получить роли пользователя
        return "User: " + username;
    }
}
