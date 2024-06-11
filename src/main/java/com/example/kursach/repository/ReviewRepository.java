package com.example.kursach.repository;

import com.example.kursach.entity.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    public Optional<Review> findByProductId(Long id);
    public Optional<Review> findByAuthorName(String authorName);
    public void deleteById(Long id);
    public List<Review> findAllByAuthorName(String authorName);
    public List<Review> findAllByProductId(Long productId);
}
