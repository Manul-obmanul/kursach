package com.example.kursach.service.impl;

import com.example.kursach.entity.Purchase;
import com.example.kursach.entity.User;
import com.example.kursach.repository.PurchaseRepository;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.repository.UserRolesRepository;
import com.example.kursach.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    public PurchaseRepository purchaseRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserRolesRepository userRolesRepository;

    @Override
    public String createPurchase(Long id, Long productId, Integer numberOfProducts, Double price, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
            Purchase purchase = Purchase.builder()
                    .id(id)
                    .productId(productId)
                    .numberOfProducts(numberOfProducts)
                    .userId(user.get().getId())
                    .price(price)
                    .dateOfPurchase(LocalDate.now())
                    .build();
            purchaseRepository.save(purchase);
            return "Покупка успешно создана";
    }

    @Override
    public String deletePurchase(Long id, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        if (purchase.isPresent() && user.get().getId() == purchase.get().getUserId()) {
            purchaseRepository.deleteById(id);
            return "Покупка успешна удалена";
        } else return "Убедитесь, что Вы вводите правильный id покупки";
    }
    @Override
    public String updatePurchase(Long id, Long productId, Integer numberOfProducts, Double price, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        if (purchase.isPresent() && purchase.get().getUserId() == user.get().getId()) {
            Purchase updatedPurchase = Purchase.builder()
                    .id(id)
                    .productId(productId)
                    .numberOfProducts(numberOfProducts)
                    .userId(user.get().getId())
                    .dateOfPurchase(purchase.get().getDateOfPurchase())
                    .price(price)
                    .build();
            purchaseRepository.save(updatedPurchase);
            return "Покупка успешно изменена";
        } else return "Убедитесь, что Вы пытаетесь изменить покупку со своим id";
    }

    @Override
    public Optional<Purchase> getPurchase(Long id, String loadName) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);
        Optional<User> user = userRepository.findByUsername(loadName);
        if(purchase.isPresent() && user.get().getId() == purchase.get().getUserId()){
            return purchase;
        } else throw new RuntimeException("Убедитесь, что Вы пытаетесь просмотреть покупку со своим id");
    }

    @Override
    public List<Purchase> getAllPurchases(String loadName){
        Optional<User> user = userRepository.findByUsername(loadName);
        List<Purchase> purchases = null;
            purchases = purchaseRepository.findAllByUserId(user.get().getId());
        if (purchases == null) {
            throw new RuntimeException("Вы еще не создали ни одной покупки");
        }
        return purchases;
    }
}
