package com.example.kursach.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.kursach.entity.UserRole;;import java.util.Optional;

public interface UserRolesRepository extends CrudRepository<UserRole, Long> {
    public UserRole findByUserId(Long id);
}
