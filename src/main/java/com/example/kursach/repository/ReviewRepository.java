package com.example.kursach.repository;

import com.example.kursach.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    public Optional<Review> findByProductId(Long id);
    public Optional<Review> findByAuthorName(String authorName);
    public void deleteById(Long id);
    public List<Review> findAllByAuthorName(String authorName);
    public List<Review> findAllByProductId(Long productId);
    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.productId = :productId")
    Double findAverageRateByProductId(@Param("productId") Long productId);
}
