package com.example.kursach.controller;

import com.example.kursach.entity.Review;
import com.example.kursach.service.impl.ReviewServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private ReviewServiceImpl reviewService;

    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping
    public ResponseEntity<Review> makeReview(@RequestParam("productId") Long productId,
                                             @RequestParam("text") String text,
                                             @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return reviewService.makeReview(productId, text, loadName);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam("id") Long id,
                                          @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return reviewService.deleteReview(id, loadName);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview (@RequestParam("id") Long id,
                                           @RequestParam("text") String text,
                                           @AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return reviewService.updateReivew(id, text, loadName);
    }
    @GetMapping("/getall")
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam("productId") Long productId){
        return reviewService.getReviews(productId);
    }
    @GetMapping("/getallmyreviews")
    public  ResponseEntity<List<Review>> getAllMyReviews(@AuthenticationPrincipal UserDetails userDetails){
        String loadName = userDetails.getUsername();
        return reviewService.getAllMyReviews(loadName);
    }
}
