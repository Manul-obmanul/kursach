package com.example.kursach.service.impl;

import com.example.kursach.entity.Review;
import com.example.kursach.entity.User;
import com.example.kursach.repository.ReviewRepository;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository repository;
    @Autowired
    private ProductServiceImpl service;
    @Override
    public ResponseEntity<Review> makeReview(Long productId, String text, Long rate, String loadName) {
        Review review = Review.builder()
                .id(null)
                .productId(productId)
                .authorName(loadName)
                .text(text)
                .dateOfCreation(LocalDate.now())
                .rate(rate)
                .build();
        service.updateRating(productId);
        return ResponseEntity.ok(review);
    }

    @Override
    public ResponseEntity<?> deleteReview(Long id, String loadName) {
        Optional<Review> review = repository.findById(id);
        if(review.isPresent()) {
            if (review.get().getAuthorName().equals(loadName)) {
                repository.deleteById(id);
                return ResponseEntity.ok("Отзыв удалён");
            } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Убедитесь, что именно Вы оставляли отзыв с таким Id");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Убедитесь, что отзыв с таким id существует");
    }

    @Override
    public ResponseEntity<?> updateReivew(Long id, String text, Long rate, String loadName) {
        Optional<Review> review = repository.findById(id);
        if(review.isPresent()) {
            if (review.get().getAuthorName().equals(loadName)) {
                LocalDate currentDate = LocalDate.now();
                long days = ChronoUnit.DAYS.between(review.get().getDateOfCreation(), currentDate);
                if (days > 1) {
                    Review updatedReview = Review.builder()
                            .id(review.get().getId())
                            .productId(review.get().getProductId())
                            .authorName(loadName)
                            .text(text)
                            .dateOfCreation(review.get().getDateOfCreation())
                            .rate(rate)
                            .build();
                    repository.save(updatedReview);
                    service.updateRating(review.get().getProductId());
                    return ResponseEntity.ok(updatedReview);
                } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("К сожалению, изменять отзыв больше нельзя");
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Убедитесь, что Вы пытаетесь изменить покупку со своим id");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Убедитесь, что отзыв с таким id существует");
    }

    @Override
    public ResponseEntity<List<Review>> getReviews(Long productId) {
        List<Review> reviews = repository.findAllByProductId(productId);
        return ResponseEntity.ofNullable(reviews);
    }

    @Override
    public ResponseEntity<List<Review>> getAllMyReviews(String loadName) {
        List<Review> reviews = repository.findAllByAuthorName(loadName);
        return ResponseEntity.ofNullable(reviews);
    }
}
