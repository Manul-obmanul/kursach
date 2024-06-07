package com.example.kursach.service.impl;

import com.example.kursach.entity.*;
import com.example.kursach.repository.ProductRepository;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.repository.UserRolesRepository;
import com.example.kursach.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    public ProductRepository productRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserRolesRepository userRolesRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public String createProduct(Long id, String brand, String category, String description, String image_name,String name,Double price, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if(userRole.get().getUserAuthority().equals(UserAuthority.MANAGE_ORDERS) ||userRole.get().getUserAuthority().equals(UserAuthority.FULL)) {
                Product product = Product.builder()
                        .id(id)
                        .brand(brand)
                        .category(category)
                        .description(description)
                        .creation_date(LocalDate.now())
                        .image_name(image_name)
                        .name(name)
                        .price(price)
                        .build();
                productRepository.save(product);
                return "Продукт успешно сохранён";
        } else return "Убедитесь, что Вы обладаете правами на создание продуктов";
    }

    @Override
    public String updateProduct(Long id, String brand, String category, String description, String image_name,String name,Double price, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<Product> product = productRepository.findById(id);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if (product.isPresent() && userRole.isPresent()) {
            if(userRole.get().getUserAuthority().equals(UserAuthority.MANAGE_ORDERS) ||userRole.get().getUserAuthority().equals(UserAuthority.FULL)) {
                Product updatedProduct = Product.builder()
                        .id(id)
                        .brand(brand)
                        .category(category)
                        .description(description)
                        .creation_date(product.get().getCreation_date())
                        .image_name(image_name)
                        .name(name)
                        .price(price)
                        .build();
                productRepository.save(updatedProduct);
                return "Покупка успешно изменена";
            } else return "Убедитесь, что Вы обладаете правами на изменение продуктов";
        } else return "Убедитесь, что Вы пытаетесь изменить существующий продукт";
    }

    @Override
    public String deleteProduct(Long id, String loadName) {
        Optional<Product> product = productRepository.findById(id);
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if (product.isPresent()&& userRole.isPresent()) {
            if(userRole.get().getUserAuthority().equals(UserAuthority.MANAGE_ORDERS) ||userRole.get().getUserAuthority().equals(UserAuthority.FULL)) {
                productRepository.deleteById(id);
                return "Продукт успешно удалён";
            } else return "Убедитесь, что Вы обладаете правами на удаление продуктов";
        } else return "Убедитесь, что Вы пытаетесь удалить существующий продукт";
    }

    @Override
    public List<Product> getAllProducts() {//TODO ВЫВОД СООБЩЕНИЕ О ПУСТОМ ЛИСТЕ
        List<Product> products = productRepository.findAll();
        return products;
    }
}
