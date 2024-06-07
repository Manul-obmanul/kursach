package com.example.kursach.controller;

import com.example.kursach.entity.Product;
import com.example.kursach.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id){
        return productService.getProduct(id);
    }

    @GetMapping("/getall")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping
    public String createProduct(@RequestParam("id")  Long id,
                                @RequestParam("brand") String brand,
                                @RequestParam("category") String category,
                                @RequestParam("description") String description,
                                @RequestParam("image_name") String image_name,
                                @RequestParam("name") String name,
                                @RequestParam("price") Double price,
                                @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        productService.createProduct(id, brand, category, description, image_name, name, price, loadName);
        return "Продукт успешно сохранён";
    }

    @PutMapping("/update")
    public String updateProduct(@RequestParam("id")  Long id,
                                @RequestParam("brand") String brand,
                                @RequestParam("category") String category,
                                @RequestParam("description") String description,
                                @RequestParam("image_name") String image_name,
                                @RequestParam("name") String name,
                                @RequestParam("price") Double price,
                                @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return productService.updateProduct(id, brand, category, description, image_name, name, price, loadName);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return productService.deleteProduct(id, loadName);
    }
}
