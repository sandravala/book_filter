package com.sv.book_search.repository;

import com.sv.book_search.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookRepositoryParameterizedTest  {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {

        bookRepository.deleteAll();
        // Create and save some test data
        bookRepository.save(new Book(null, "Book 1", "Petras P", 2008, 4.9, "kazkoks aprasymas", "Technology"));
        bookRepository.save(new Book(null, "Book 2", "Roberta Ma", 2021, 3, "tingiu rasyt aprrasymus", "Fiction"));
        bookRepository.save(new Book(null, "Book 3", "Andrius Jas", 2024, 5, "daug visokiu zodziu",
                "Psychology"));
    }

    static Stream<StringTestScenario> provideNoResultsScenarios() {
        return Stream.of(
                new StringTestScenario("title", "non-existent title"),
                new StringTestScenario("author", "unknown author"),
                new StringTestScenario("genre", "unknown genre"),
                new StringTestScenario("year", 1800),
                new StringTestScenario("description", "non-existent description")
        );
    }

    static Stream<StringTestScenario> provideNonEmptyResultsScenarios() {
        return Stream.of(
                new StringTestScenario("title", "book"),
                new StringTestScenario("author", "Petras"),
                new StringTestScenario("genre", "Technology"),
                new StringTestScenario("year", 2024),
                new StringTestScenario("description", "aprasym")
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

        Assertions.assertThat(bookList).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideNonEmptyResultsScenarios")
    public void testNonEmptyResultsFound(StringTestScenario scenario) {

        List<Book> bookList = getBooksBasedOnScenario(scenario);

        Assertions.assertThat(bookList).isNotEmpty();

        for (Book book : bookList) {
            switch (scenario.method) {
                case "title" -> Assertions.assertThat(book.getTitle()).containsIgnoringCase((String) scenario.value);
                case "author" -> Assertions.assertThat(book.getAuthor()).containsIgnoringCase((String) scenario.value);
                case "genre" -> Assertions.assertThat(book.getGenre()).isEqualToIgnoringCase((String) scenario.value);
                case "year" -> Assertions.assertThat(book.getPublicationYear()).isEqualTo((Integer) scenario.value);
                case "description" -> Assertions.assertThat(book.getDescription()).containsIgnoringCase((String) scenario.value);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideNoResultsRatingRangeScenarios")
    public void testNoRangesFound(RatingRangeScenario scenario) {

        List<Book> books = bookRepository.findByRatingBetween(scenario.minRating, scenario.maxRating);

        Assertions.assertThat(books).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideNonEmptyResultsRatingRangeScenarios")
    public void testNonEmptyRangesFound(RatingRangeScenario scenario) {

        List<Book> books = bookRepository.findByRatingBetween(scenario.minRating, scenario.maxRating);

        Assertions.assertThat(books).isNotEmpty();
        for (Book book : books) {
            Assertions.assertThat(book.getRating()).isBetween(scenario.minRating, scenario.maxRating);
        }
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
            case "description" -> bookRepository.findByDescriptionContainingIgnoreCase((String) scenario.value);
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
