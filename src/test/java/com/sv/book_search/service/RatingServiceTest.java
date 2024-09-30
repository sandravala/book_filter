package com.sv.book_search.service;

import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import com.sv.book_search.repository.RatingRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    RatingRepository ratingRepository;
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    RatingService ratingService;

    private Rating rating;
    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        book = Book.builder()
                .id(1L)
                .publicationYear(2008)
                .genre("Technology")
                .author("Brian Joshua Bloch")
                .title("Effective Java")
                .description("A comprehensive guide to Java")
                .build();

        rating = Rating.builder()
                .rating(5)
                .book(book)
                .username("user1@example.com")
                .build();

    }

    @Test
    public void testSaveRatingForBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating savedRating = ratingService.saveRatingForBook(1L, 5.0, "user1@example.com");

        Assertions.assertThat(savedRating).isEqualTo(rating);
        Assertions.assertThat(savedRating.getBook()).isEqualTo(book);
        Assertions.assertThat(savedRating.getRating()).isEqualTo(5.0);
        Assertions.assertThat(savedRating.getUsername()).isEqualTo("user1@example.com");

        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveRatingForBook_BookNotFound() {
        // Arrange: Simulate the repository behavior for a non-existent book ID
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert: Expect a RuntimeException when the book is not found
        Assertions.assertThatThrownBy(() -> ratingService.saveRatingForBook(2L, 4.0, "user2@example.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book not found with ID: 2");

        // Verify that the save method was never called
        verify(ratingRepository, never()).save(any(Rating.class));
        verify(bookRepository, times(1)).findById(2L);
    }
}
