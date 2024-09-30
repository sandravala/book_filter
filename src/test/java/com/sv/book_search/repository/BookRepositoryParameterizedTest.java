package com.sv.book_search.repository;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.service.BookSpecifications;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookRepositoryParameterizedTest  {

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

        // Create book instances without ratings first
        book1 = Book.builder()
                .title("Book 1")
                .author("Petras P")
                .publicationYear(2008)
                .description("kazkoki aprasyma")
                .genre("Technology")
                .build();

        book2 = Book.builder()
                .title("Book 2")
                .author("Roberta Ma")
                .publicationYear(2021)
                .description("tingiu rasyt aprasyma")
                .genre("Fiction")
                .build();

        book3 = Book.builder()
                .title("Book 3")
                .author("Andrius Jas")
                .publicationYear(2024)
                .description("daug visokiu zodziu")
                .genre("Psychology")
                .build();

        // Save books to get their generated IDs
        book1 = bookRepository.save(book1);
        book2 = bookRepository.save(book2);
        book3 = bookRepository.save(book3);

        // Create ratings and associate them with the books
        Rating rating1 = Rating.builder()
                .rating(5)
                .username("user1@example.com")
                .book(book1) // Associate with book1
                .build();

        Rating rating2 = Rating.builder()
                .rating(3)
                .username("user2@example.com")
                .book(book2) // Associate with book2
                .build();

        // Save ratings to ensure they are associated with the correct books
        ratingRepository.save(rating1);
        ratingRepository.save(rating2);

        // Create a modifiable list for book3's ratings and add ratings
        List<Rating> book3Ratings = new ArrayList<>();
        book3Ratings.add(rating1);
        book3Ratings.add(rating2);

        // Set the modifiable list of ratings for book3
        book3.setRatings(book3Ratings);

        // Save book3 with the associated ratings
        bookRepository.save(book3);
    }



    static Stream<StringTestScenario> provideNoResultsScenarios() {
        return Stream.of(
                new StringTestScenario("title", "non-existent title"),
                new StringTestScenario("author", "unknown author"),
                new StringTestScenario("genre", "unknown genre"),
                new StringTestScenario("year", 1800)
        );
    }

    static Stream<StringTestScenario> provideNonEmptyResultsScenarios() {
        return Stream.of(
                new StringTestScenario("title", "book"),
                new StringTestScenario("author", "Petras"),
                new StringTestScenario("genre", "Technology"),
                new StringTestScenario("year", 2024)
        );
    }

    static Stream<RatingRangeScenario> provideNoResultsRatingRangeScenarios() {
        return Stream.of(
                new RatingRangeScenario(0, 1.0),
                new RatingRangeScenario(0, 2.0),
                new RatingRangeScenario(3.5, 4.0)
        );
    }

    static Stream<RatingRangeScenario> provideNonEmptyResultsRatingRangeScenarios() {
        return Stream.of(
                new RatingRangeScenario(0, 5.0),
                new RatingRangeScenario(4.0, 5.0),
                new RatingRangeScenario(3.0, 5.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNoResultsScenarios")
    public void testNoResultsFound(StringTestScenario scenario) {
        List<Book> bookList = getBooksBasedOnScenario(scenario);

        assertThat(bookList).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideNonEmptyResultsScenarios")
    public void testNonEmptyResultsFound(StringTestScenario scenario) {

        List<Book> bookList = getBooksBasedOnScenario(scenario);

        assertThat(bookList).isNotEmpty();

        for (Book book : bookList) {
            switch (scenario.method) {
                case "title" -> assertThat(book.getTitle()).containsIgnoringCase((String) scenario.value);
                case "author" -> assertThat(book.getAuthor()).containsIgnoringCase((String) scenario.value);
                case "genre" -> assertThat(book.getGenre()).isEqualToIgnoringCase((String) scenario.value);
                case "year" -> assertThat(book.getPublicationYear()).isEqualTo((Integer) scenario.value);
            }
        }
    }


    @Test
    public void testFindAllWithDescriptionKeywords() {
        List<String> keywords1 = Arrays.asList("aprasyma", "kazkoki");
        List<String> keywords2 = Arrays.asList("aprasyma");
        Specification<Book> spec1 = BookSpecifications.descriptionContainsAllKeywords(keywords1);
        Specification<Book> spec2 = BookSpecifications.descriptionContainsAllKeywords(keywords2);

        List<Book> foundBooks1 = bookRepository.findAll(spec1);
        List<Book> foundBooks2 = bookRepository.findAll(spec2);

        Assertions.assertThat(foundBooks1).contains(book1);
        Assertions.assertThat(foundBooks2).containsExactlyInAnyOrder(book1, book2);
    }

    private List<Book> getBooksBasedOnScenario(StringTestScenario scenario) {

        if (scenario.value == null) {
            return Collections.emptyList();
        }

        return switch (scenario.method) {
            case "title" -> bookRepository.findByTitleContainingIgnoreCase((String) scenario.value);
            case "author" -> bookRepository.findByAuthorContainingIgnoreCase((String) scenario.value);
            case "genre" -> bookRepository.findByGenreIgnoreCase((String) scenario.value);
            case "year" -> {
                if (scenario.value instanceof Integer year) {
                    yield bookRepository.findByPublicationYear(year);
                } else {
                    yield Collections.emptyList();
                }
            }
            default -> Collections.emptyList();
        };

    }

    private static class StringTestScenario {
        String method;
        Object value;

        StringTestScenario(String method, Object value) {
            this.method = method;
            this.value = value;
        }
    }

    private static class RatingRangeScenario {
        double minRating;
        double maxRating;


        RatingRangeScenario(double minRating, double maxRating) {
            this.minRating = minRating;
            this.maxRating = maxRating;
        }
    }

}
