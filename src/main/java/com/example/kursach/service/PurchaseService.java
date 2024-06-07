package com.example.kursach.service;


import com.example.kursach.entity.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {
    public String createPurchase(Long id, Long productId, Integer numberOfProducts, Double price, String loadName);
    public String deletePurchase(Long id, String loadName);
    public List<Purchase> getAllPurchases(String loadName);
    public String updatePurchase(Long id, Long productId, Integer numberOfProducts, Double price, String loadName);
    public Optional<Purchase> getPurchase(Long id, String loadName);
}
