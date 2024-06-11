package com.example.kursach.service;

import com.example.kursach.entity.Product;
import com.example.kursach.entity.User;
import com.example.kursach.entity.UserAuthority;
import com.example.kursach.entity.UserRole;
import com.example.kursach.repository.ProductRepository;
import com.example.kursach.repository.PurchaseRepository;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.repository.UserRolesRepository;
import com.example.kursach.service.impl.ProductServiceImpl;
import com.example.kursach.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @BeforeEach
    public void setup(){
        userRepository = Mockito.mock(UserRepository.class);
        userRolesRepository = Mockito.mock(UserRolesRepository.class);
        userService = new UserServiceImpl(userRepository, userRolesRepository, purchaseRepository);
        productService = new ProductServiceImpl(productRepository);
    }
    @Mock
    UserRepository userRepository;
    PurchaseRepository purchaseRepository;
    UserServiceImpl userService;
    @Mock
    UserRolesRepository userRolesRepository;
    @InjectMocks
    ProductServiceImpl productService;
    @Mock
    ProductRepository productRepository;
    @Test
    public void testCreateProduct() {
        MockitoAnnotations.initMocks(this);
        UserRepository userRepository = mock(UserRepository.class);
        UserRolesRepository userRolesRepository = mock(UserRolesRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, userRolesRepository, purchaseRepository);
        User user = new User();
        user.setUsername("test"); user.setPassword("test");
        userRepository.save(user);
        lenient().when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserRole userRole = new UserRole();
        userRole.setUserAuthority(UserAuthority.MANAGE_ORDERS);
        lenient().when(userRolesRepository.findByUserId(user.getId())).thenReturn(userRole);

        String result = String.valueOf(productService.createProduct(1L, "Brand", "Category", "Description", "image.jpg", "Product", 100.0, "testUser"));

        assertEquals("Продукт успешно сохранён", result);
        verify(productRepository, times(1)).save(any(Product.class));
    }
    @Test
    public void testUpdateProduct() {
        UserRepository userRepository = mock(UserRepository.class);;
        UserRolesRepository userRolesRepository = mock(UserRolesRepository.class);;
        UserServiceImpl userService = new UserServiceImpl(userRepository, userRolesRepository, purchaseRepository);
        Long id = 1L;
        String brand = "brand";
        String category = "category";
        String description = "description";
        String image_name = "image_name";
        String name = "name";
        Double price = 10.0;

        User user = new User();
        user.setId(1L);
        lenient().when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Product product = new Product();
        product.setId(id);
        product.setBrand("oldBrand");
        lenient().when(productRepository.findById(id)).thenReturn(Optional.of(product));

        String result = String.valueOf(productService.updateProduct(id, brand, category, description, image_name, name, price, "testUser"));

        assertEquals("Покупка успешно изменена", result);
        assertThat(product.getBrand()).isEqualTo("oldBrand");
    }
    @Test
    public void deleteProductTest() {
        UserRepository userRepository = mock(UserRepository.class);;
        UserRolesRepository userRolesRepository = mock(UserRolesRepository.class);;
        UserServiceImpl userService = new UserServiceImpl(userRepository, userRolesRepository, purchaseRepository);
        Product product = new Product();
        product.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        UserRole userRole = new UserRole();
        userRole.setId(1L);
        userRole.setUserAuthority(UserAuthority.MANAGE_ORDERS);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(userRolesRepository.findByUserId(1L)).thenReturn(userRole);


        String result = String.valueOf(productService.deleteProduct(1L, "testUser"));

        assertEquals("Продукт успешно удалён", result);
    }
}
