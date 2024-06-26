package com.example.kursach.repository;

import com.example.kursach.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.StyleSheet;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findAllByPriceBetween(Double min, Double max);
    public List<Product> findAllByBrand(String brand);
    public List<Product> findAllByRatingGreaterThan(Double rating);
}
