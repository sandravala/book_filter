package com.sv.book_search.service;

import com.sv.book_search.dto.RatingRequest;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import com.sv.book_search.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RatingServiceIntegrationTest {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2020);
        book.setDescription("Test Description");
        book.setGenre("Fiction");

        book = bookRepository.save(book);
    }

    @Test
    public void testSaveRatingForBook() {
        RatingRequest ratingRequest = new RatingRequest(4.5, "testuser@example.com");
        List<Rating> ratingsBefore = ratingRepository.findAll();

        Rating savedRating = ratingService.saveRatingForBook(book.getId(), ratingRequest.getRatingValue(), ratingRequest.getUsername());

        Long ratingId = savedRating.getId();

        assertThat(savedRating).isNotNull();
        assertThat(savedRating.getRating()).isEqualTo(4.5);
        assertThat(savedRating.getUsername()).isEqualTo("testuser@example.com");
        assertThat(savedRating.getBook().getId()).isEqualTo(book.getId());

        List<Rating> ratingsAfter = ratingRepository.findAll();
        assertThat(ratingsAfter).hasSize(ratingsBefore.size() + 1);
    }
}
