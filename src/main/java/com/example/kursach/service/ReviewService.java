package com.example.kursach.service;

import com.example.kursach.entity.Review;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {
    public ResponseEntity<Review> makeReview(Long productId, String text, String loadName);
    public ResponseEntity<?> deleteReview(Long id, String loadName);
    public ResponseEntity<?> updateReivew(Long id, String text, String loadName);
    public  ResponseEntity<List<Review>> getReviews(Long productId);
    public ResponseEntity<List<Review>> getAllMyReviews(String loadName);
}
