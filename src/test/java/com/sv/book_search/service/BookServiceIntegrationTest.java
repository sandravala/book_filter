package com.sv.book_search.service;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import com.sv.book_search.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookServiceIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RatingRepository ratingRepository;

    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    public void setUp() {
        ratingRepository.deleteAll();
        bookRepository.deleteAll();

        book1 = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .publicationYear(2008)
                .description("A comprehensive guide to Java")
                .genre("Technology")
                .build();

        book2 = Book.builder()
                .title("Java Concurrency in Practice")
                .author("Brian Goetz")
                .publicationYear(2015)
                .description("A book on Java concurrency")
                .genre("Technology")
                .build();

        book3 = Book.builder()
                .title("Steve Jobs")
                .author("Walter Isaacson")
                .publicationYear(2011)
                .description("The biography of Apple co-founder Steve Jobs")
                .genre("Biography")
                .build();

        book1 = bookRepository.save(book1);
        book2 = bookRepository.save(book2);
        book3 = bookRepository.save(book3);

        Rating rating1 = Rating.builder().rating(5.0).book(book1).username("user1@example.com").build();
        Rating rating2 = Rating.builder().rating(3.0).book(book1).username("user2@example.com").build();
        Rating rating3 = Rating.builder().rating(4.0).book(book2).username("user3@example.com").build();

        ratingRepository.saveAll(Arrays.asList(rating1, rating2, rating3));
        book1.setRatings(Arrays.asList(rating1, rating2));
        book2.setRatings(Arrays.asList(rating3));
    }

    @Test
    public void testFilterBooksByTitle() {
        List<BookWithAverageRatingDTO> foundBooks = bookService.filterBooks("Effective Java", null, null, null, null, null, null);

        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("Effective Java");
        assertThat(foundBooks.get(0).getAverageRating()).isEqualTo(4.0);
    }

    @Test
    public void testFilterBooksByYear() {
        List<BookWithAverageRatingDTO> foundBooks = bookService.filterBooks(null, null, null, 2015, null, null, null);

        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("Java Concurrency in Practice");
    }

    @Test
    public void testFilterBooksByGenre() {
        List<BookWithAverageRatingDTO> foundBooks = bookService.filterBooks(null, null, null, null, null, "Technology", null);

        assertThat(foundBooks).hasSize(2);
    }

    @Test
    public void testFilterBooksByRatingRange() {
        List<BookWithAverageRatingDTO> foundBooks = bookService.filterBooks(null, 4, 5, null, null, null, null);

        assertThat(foundBooks).hasSize(2);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("Effective Java");
    }

    @Test
    public void testFilterBooksWithNoRatings() {
        // Book 3 has no ratings, and should still be returned with an average rating of 0
        List<BookWithAverageRatingDTO> foundBooks = bookService.filterBooks(null, null, null, null, "Walter Isaacson", null, null);

        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("Steve Jobs");
        assertThat(foundBooks.get(0).getAverageRating()).isEqualTo(0.0);
    }
}
