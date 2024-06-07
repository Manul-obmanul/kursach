package com.example.kursach.repository;

import com.example.kursach.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    Optional<Purchase> findByUserId(Long aLong);
    public List<Purchase> findAllByUserId(Long id);
}
