package com.example.kursach.service;

import com.example.kursach.entity.Purchase;
import com.example.kursach.entity.User;
import com.example.kursach.repository.PurchaseRepository;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.service.impl.PurchaseServiceImpl;
import com.example.kursach.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {
    @BeforeEach
    public void setup(){
        purchaseRepository = Mockito.mock(PurchaseRepository.class);
        purchaseService = new PurchaseServiceImpl();
    }
    PurchaseService purchaseService;
    PurchaseRepository purchaseRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @Test
    public void testCreatePurchase() {
        Long id = 123L;
        Long productId = 456L;
        Integer numberOfProducts = 2;
        Double price = 10.0;
        String loadName = "username";

        User user = new User();
        user.setId(789L);

        when(userRepository.findByUsername(loadName)).thenReturn(Optional.of(user));

        String result = purchaseService.createPurchase(id, productId, numberOfProducts, price, loadName);

        assertEquals("Покупка успешно создана", result);
    }
    @Test
    public void testDeletePurchase() {
        Long purchaseId = 1L;
        String loadName = "testUser";

        User user = new User();
        user.setId(1L);
        user.setUsername(loadName);

        Purchase purchase = new Purchase();
        purchase.setId(purchaseId);
        purchase.setUserId(1L);

        when(userRepository.findByUsername(loadName)).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(purchaseId)).thenReturn(Optional.of(purchase));

        String result = purchaseService.deletePurchase(purchaseId, loadName);

        assertEquals("Покупка успешна удалена", result);
    }
    @Test
    public void testGetPurchase() {
        User user = new User(); // Создаем заглушку пользователя
        user.setId(1L);
        user.setUsername("exampleUser");

        Purchase purchase = new Purchase(); // Создаем заглушку покупки
        purchase.setId(1L);
        purchase.setUserId(1L);

        when(userRepository.findByUsername("exampleUser")).thenReturn(Optional.of(user));
        when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));

        Optional<Purchase> result = purchaseService.getPurchase(1L, "exampleUser");

        Assertions.assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId().longValue());
        assertEquals(1L, result.get().getUserId().longValue());
    }
    @Test
    public void testGetAllPurchases() {
        User user = new User();
        user.setUsername("testUser");
        user.setId(1L);

        List<Purchase> purchases = new ArrayList<>();
        Purchase purchase1 = new Purchase();
        purchase1.setId(1L);
        purchase1.setUserId(1L);
        Purchase purchase2 = new Purchase();
        purchase2.setId(2L);
        purchase2.setUserId(1L);
        purchases.add(purchase1);
        purchases.add(purchase2);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(purchaseRepository.findAllByUserId(1L)).thenReturn(purchases);

        List<Purchase> result = purchaseService.getAllPurchases("testUser");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId().longValue());
        assertEquals(1L, result.get(0).getUserId().longValue());
        assertEquals(2L, result.get(1).getId().longValue());
        assertEquals(1L, result.get(1).getUserId().longValue());
    }
}
