package com.example.kursach.service.impl;

import com.example.kursach.entity.*;
import com.example.kursach.repository.ProductRepository;
import com.example.kursach.repository.ReviewRepository;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.repository.UserRolesRepository;
import com.example.kursach.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    @Autowired
    public ReviewRepository reviewRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public ResponseEntity<?> getProduct(Long id) {
        return ResponseEntity.ok(productRepository.findById(id).get());
    }

    @Override
    public ResponseEntity<?> createProduct(Long id, String brand, String category, String description, String image_name,String name,Double price, String loadName) {
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
                        .rating(null)
                        .build();
                productRepository.save(product);
                return ResponseEntity.ok(product);
        } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Убедитесь, что Вы обладаете правами на создание продуктов");
    }

    @Override
    public ResponseEntity<?> updateProduct(Long id, String brand, String category, String description, String image_name,String name,Double price, String loadName) {
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
                        .rating(product.get().getRating())
                        .build();
                productRepository.save(updatedProduct);
                return ResponseEntity.ok(updatedProduct);
            } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Убедитесь, что Вы обладаете правами на изменение продуктов");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Убедитесь, что Вы пытаетесь изменить существующий продукт");
    }
    @Override
    public void updateRating(Long id){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product updatedProduct = Product.builder()
                    .id(id)
                    .brand(product.get().getBrand())
                    .category(product.get().getCategory())
                    .description(product.get().getDescription())
                    .creation_date(product.get().getCreation_date())
                    .image_name(product.get().getImage_name())
                    .name(product.get().getName())
                    .price(product.get().getPrice())
                    .rating(reviewRepository.findAverageRateByProductId(id))
                    .build();
            productRepository.save(updatedProduct);
        }
    }
    @Override
    public ResponseEntity<?> deleteProduct(Long id, String loadName) {
        Optional<Product> product = productRepository.findById(id);
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if (product.isPresent()&& userRole.isPresent()) {
            if(userRole.get().getUserAuthority().equals(UserAuthority.MANAGE_ORDERS) ||userRole.get().getUserAuthority().equals(UserAuthority.FULL)) {
                productRepository.deleteById(id);
                return ResponseEntity.ok(product.get());
            } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Убедитесь, что Вы обладаете правами на удаление продуктов");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Убедитесь, что Вы пытаетесь удалить существующий продукт");
    }

    @Override
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ofNullable(products);
    }
    @Override
    public ResponseEntity<List<Product>> getProductsByPrice(Double min, Double max){
        List<Product> products = productRepository.findAllByPriceBetween(min, max);
        return ResponseEntity.ofNullable(products);
    }
    @Override
    public ResponseEntity<List<Product>> getProductsByBrand(String brand){
        List<Product> products = productRepository.findAllByBrand(brand);
        return ResponseEntity.ofNullable(products);
    }
    @Override
    public ResponseEntity<List<Product>> getProductsByRating(Double rating){
        List<Product> products = productRepository.findAllByRatingGreaterThan(rating);
        return ResponseEntity.ofNullable(products);
    }
}
