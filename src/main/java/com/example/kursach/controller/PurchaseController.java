package com.example.kursach.controller;

import com.example.kursach.entity.Purchase;
import com.example.kursach.service.impl.PurchaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    public final PurchaseServiceImpl purchaseService;

    public PurchaseController(PurchaseServiceImpl purchaseService) {
        this.purchaseService = purchaseService;
    }


    @PostMapping
    public String createPurchase(@RequestParam("id")  Long id,
                                @RequestParam("productId") Long productId,
                                @RequestParam("numberOfProducts") Integer numberOfProducts,
                                @RequestParam("price") Double price,
                                 @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return purchaseService.createPurchase(id, productId, numberOfProducts, price, loadName);
    }

    @PutMapping("/update")
    public String updatePurchase(@RequestParam("id")  Long id,
                                @RequestParam("productId") Long productId,
                                @RequestParam("numberOfProducts") Integer numberOfProducts,
                                @RequestParam("price") Double price,
                                 @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return purchaseService.updatePurchase(id, productId, numberOfProducts, price, loadName);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePurchase(@PathVariable("id") Long id,
                                 @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return purchaseService.deletePurchase(id, loadName);
    }

    @GetMapping("/{id}")
    public Optional<Purchase> getPurchase(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return purchaseService.getPurchase(id, loadName);
    }

    @GetMapping("/getall")
    public List<Purchase> getAllPurchases(@AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return purchaseService.getAllPurchases(loadName);
    }
}
