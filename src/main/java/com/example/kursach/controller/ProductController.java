package com.example.kursach.controller;

import com.example.kursach.entity.Product;
import com.example.kursach.service.ProductService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id){
        return productService.getProduct(id);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Product>> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestParam("id")  Long id,
                                                  @RequestParam("brand") String brand,
                                                  @RequestParam("category") String category,
                                                  @RequestParam("description") String description,
                                                  @RequestParam("image_name") String image_name,
                                                  @RequestParam("name") String name,
                                                  @RequestParam("price") Double price,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return productService.createProduct(id, brand, category, description, image_name, name, price, loadName);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestParam("id")  Long id,
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
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return productService.deleteProduct(id, loadName);
    }

    @GetMapping("/getbyprice")
    public ResponseEntity<?> getByPrice(@RequestParam("minprice") Double min,
                                        @RequestParam("maxprice") Double max){
        return productService.getProductsByPrice(min, max);
    }

    @GetMapping("/getbybrand")
    public ResponseEntity<?> getByBrand(@RequestParam("brand") String brand){
        return productService.getProductsByBrand(brand);
    }

    @GetMapping("/getbyrating")
    public ResponseEntity<?> getByRating(@RequestParam("rating") Double rating){
        return productService.getProductsByRating(rating);
    }
}
