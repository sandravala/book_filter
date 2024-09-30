package com.sv.book_search.repository;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.service.BookSpecifications;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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

        book1 = Book.builder()
                .title("Book 1")
                .author("Petras Ma P")
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

        book1 = bookRepository.save(book1);
        book2 = bookRepository.save(book2);
        book3 = bookRepository.save(book3);

        Rating rating1 = Rating.builder()
                .rating(5)
                .username("user1@example.com")
                .book(book1)
                .build();

        Rating rating2 = Rating.builder()
                .rating(3)
                .username("user2@example.com")
                .book(book1)
                .build();
        Rating rating3 = Rating.builder()
                .rating(5)
                .username("user3@example.com")
                .book(book3)
                .build();

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
        ratingRepository.save(rating3);

        book1.setRatings(new ArrayList<>(Arrays.asList(rating1, rating2)));
        book3.setRatings(new ArrayList<>(Arrays.asList(rating3)));

        bookRepository.save(book1);
        bookRepository.save(book3);
    }


    private static Stream<Arguments> provideFilterArguments() {
        return Stream.of(
                Arguments.of(null, null, null, null, null, null, Arrays.asList("aprasyma", "kazkoki"),
                        Arrays.asList(0)), // Keywords
                Arguments.of(null, null, null, 2008, null, null, null, Arrays.asList(0)), // Year
                Arguments.of(null, null, null, null, "ma", null, null, Arrays.asList(0, 1)), // Author
                Arguments.of(null, null, null, null, null, "technology", null, Arrays.asList(0)), // Genre
                Arguments.of("book 1", null, null, null, null, null, null, Arrays.asList(0)), // Title
                Arguments.of(null, null, 4, null, null, null, null, Arrays.asList(0, 1)), // rating
                Arguments.of("book", 4, 5, 2024, "andrius", "psychology", Arrays.asList("daug"), Arrays.asList(2))
                // all
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilterArguments")
    public void testFindWithDifferentFilters(String title, Integer minRating, Integer maxRating, Integer year,
                                             String author, String genre, List<String> keywords,
                                             List<Integer> booksToGet) {
        Specification<Book> spec = BookSpecifications.filterBooks(title, minRating, maxRating, year, author, genre, keywords);

        List<Book> allBooks = Arrays.asList(book1, book2, book3);
        List<Book> expectedBooks = booksToGet.stream()
                .map(index -> allBooks.get(index))
                .collect(Collectors.toList());


        List<Book> foundBooks = bookRepository.findAll(spec);
        Assertions.assertThat(foundBooks).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }



}
