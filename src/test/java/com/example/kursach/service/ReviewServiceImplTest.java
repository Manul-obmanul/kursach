package com.example.kursach.service;

import com.example.kursach.entity.Review;
import com.example.kursach.repository.ReviewRepository;
import com.example.kursach.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {
    @BeforeEach
    public void setup(){
        repository = Mockito.mock(ReviewRepository.class);
        reviewService = Mockito.mock(ReviewServiceImpl.class);
    }
    ReviewRepository repository;
    ReviewServiceImpl reviewService;
    @Test
    public void testMakeReview() {
        Long productId = 1L;
        String text = "Тестовый текст";
        String authorName = "user";
        Review expectedReview = Review.builder()
                .id(null)
                .productId(productId)
                .authorName(authorName)
                .text(text)
                .build();
        ResponseEntity<Review> response = reviewService.makeReview(productId, text, authorName);

        assertEquals(expectedReview, response.getBody());
    }
    @Test
    void deleteReview_Ok() {

        Long id = 1L;
        String authorName = "user";
        Review review = new Review();
        review.setId(id);
        review.setAuthorName(authorName);

        when(repository.findById(id)).thenReturn(Optional.of(review));

        ResponseEntity<?> response = reviewService.deleteReview(id, authorName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Отзыв удалён", response.getBody());
    }

    @Test
    void deleteReview_Forbidden() {
        Long id = 1L;
        String authorName = "user";
        Review review = new Review();
        review.setId(id);
        review.setAuthorName("user");

        when(repository.findById(id)).thenReturn(Optional.of(review));

        ResponseEntity<?> response = reviewService.deleteReview(id, authorName);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Убедитесь, что именно Вы оставляли отзыв с таким Id", response.getBody());
    }

    @Test
    void deleteReview_NotFound() {

        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = reviewService.deleteReview(id, "user");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Убедитесь, что отзыв с таким id существует", response.getBody());
    }
    @Test
    public void testUpdateReview() {
        Review review = new Review(1L,"user", 1L,  "Тестовый текст");
        repository.save(review);

        ResponseEntity<?> response = reviewService.updateReivew(1L, "Новый тестовый текст!", "user");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Review updatedReview = (Review) response.getBody();
        assertNotNull(updatedReview);
        assertEquals("Новый тестовый текст!", updatedReview.getText());
    }
}
