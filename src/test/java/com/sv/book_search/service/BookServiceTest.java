package com.sv.book_search.service;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    private List<Book> books;
    private List<BookWithAverageRatingDTO> allBooks;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Rating> ratings;

        Book book1 = Book.builder()
                .title("Book 1")
                .id(1L)
                .author("Petras Ma P")
                .publicationYear(2008)
                .description("kazkoki aprasyma")
                .genre("Technology")
                .build();

        Book book2 = Book.builder()
                .title("Book 2")
                .id(2L)
                .author("Roberta Ma")
                .publicationYear(2021)
                .description("tingiu rasyt aprasyma")
                .genre("Fiction")
                .build();

        Book book3 = Book.builder()
                .title("Book 3")
                .id(3L)
                .author("Andrius Jas")
                .publicationYear(2024)
                .description("daug visokiu zodziu")
                .genre("Psychology")
                .build();

        books = Arrays.asList(book1, book2, book3);
        bookRepository.saveAll(books);

        Rating rating1 = Rating.builder()
                .rating(5)
                .book(book1)
                .username("user1@example.com")
                .build();

        Rating rating2 = Rating.builder()
                .rating(3)
                .book(book1)
                .username("user2@example.com")
                .build();

        Rating rating3 = Rating.builder()
                .rating(5)
                .book(book3)
                .username("user2@example.com")
                .build();

        ratings = Arrays.asList(rating1, rating2);

        book1.setRatings(ratings);
        book3.setRatings(Collections.singletonList(rating3));

        allBooks = Arrays.asList(
                new BookWithAverageRatingDTO(1L, "Book 1", "Petras Ma P", 2008, "kazkoki aprasyma", "Technology", 4.0),
                new BookWithAverageRatingDTO(2L, "Book 2", "Roberta Ma", 2021, "tingiu rasyt aprasyma",
                        "Fiction",
                        0.0),
                new BookWithAverageRatingDTO(3L, "Book 3", "Andrius Jas", 2024, "daug visokiu zodziu",
                        "Psychology", 5.0)
        );

    }

    private static Stream<Arguments> provideFilterArguments() {
        return Stream.of(
                // keywords
                Arguments.of(null, null, null, null, null, null, Arrays.asList("aprasyma", "kazkoki"), Arrays.asList(0)),
                // year
                Arguments.of(null, null, null, 2008, null, null, null, Arrays.asList(0)),
                // author
                Arguments.of(null, null, null, null, "ma", null, null, Arrays.asList(0, 1)),
                // genre
                Arguments.of(null, null, null, null, null, "technology", null, Arrays.asList(0)),
                // title
                Arguments.of("book 1", null, null, null, null, null, null, Arrays.asList(0)),
                // rating range
                Arguments.of(null, 3, 4, null, null, null, null, Arrays.asList(0)),
                // Combined filter
                Arguments.of("book", 4, 5, 2024, "andrius", "psychology", Arrays.asList("daug"), Arrays.asList(2)),
                // all
                Arguments.of(null, null, null, null, null, null, null, Arrays.asList(0, 1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilterArguments")
    public void testFindWithDifferentFilters(String title, Integer minRating, Integer maxRating, Integer year,
                                             String author, String genre, List<String> keywords,
                                             List<Integer> booksToGet) {

        when(bookRepository.findAll(any(Specification.class)))
                .thenReturn(booksToGet.stream()
                        .map(index -> books.get(index))
                        .toList());

        List<BookWithAverageRatingDTO> expectedBooks = booksToGet.stream()
                .map(index -> allBooks.get(index))
                .toList();

        List<BookWithAverageRatingDTO> foundBooks = bookService.filterBooks(title, minRating, maxRating, year, author, genre, keywords);




        Assertions.assertThat(foundBooks).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

}
