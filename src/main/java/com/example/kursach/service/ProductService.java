package com.example.kursach.service;

import com.example.kursach.entity.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductService {
    public Product getProduct(Long id);
    public String createProduct(Long id, String brand, String category, String description, String image_name,String name,Double price, String loadname);
    public String updateProduct(Long id, String brand, String category, String description, String image_name,String name,Double price, String loadname);
    public String deleteProduct(Long id, String loadname);
    public List<Product> getAllProducts();
}
