package com.example.kursach.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    public String deleteUser(String username, String loadName);
    public String updateUser(Long id, String username, String email, Long phone, String password, String loadName);
    void registration(String firstName,String email,Long phone, String password);
    public ResponseEntity<?> getInfo(String email, String loadName);
}
