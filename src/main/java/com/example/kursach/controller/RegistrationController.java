package com.example.kursach.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.kursach.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/registration")
public class RegistrationController {
    @Autowired
    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> registration(
            @RequestParam("username") String firstname,
            @RequestParam("email") String email,
            @RequestParam("phone") Long phone,
            @RequestParam("password") String password
    ) {
        userService.registration(firstname, email, phone, password);
        return ResponseEntity.ok().build();
    }
}
