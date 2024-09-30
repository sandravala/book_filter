package com.sv.book_search.service;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.sv.book_search.service.BookSpecifications;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    private List<Book> books;
    private List<BookWithAverageRatingDTO> expectedBooks;

    @InjectMocks
    private  RatingService ratingService;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Rating> ratings;

        Book book1 = Book.builder()
                .id(1L)
                .publicationYear(2008)
                .genre("Technology")
                .author("Brian Joshua Bloch")
                .title("Effective Java")
                .description("A comprehensive guide to Java")
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .publicationYear(2024)
                .genre("Technology")
                .author("Brian Goetz")
                .title("Java Concurrency in Practice")
                .description("A book on Java concurrency")
                .build();

        Book book3 = Book.builder()
                .id(3L)
                .publicationYear(2011)
                .title("Steve Jobs")
                .description("The biography of Apple co-founder Steve Jobs a")
                .author("Walter Isaacson")
                .genre("Biography")
                .build();

        books = Arrays.asList(book1, book2, book3);

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
                .rating(2)
                .book(book2)
                .username("user2@example.com")
                .build();

        ratings = Arrays.asList(rating1, rating2);

        book1.setRatings(ratings);
        book2.setRatings(Collections.singletonList(rating3));

        expectedBooks = Arrays.asList(
                new BookWithAverageRatingDTO(1L, "Effective Java", "Brian Joshua Bloch", 2008, "A comprehensive guide" +
                        " to Java", "Technology",
                        4.0),
                new BookWithAverageRatingDTO(2L, "Java Concurrency in Practice", "Brian Goetz", 2024, "A book on Java concurrency", "Technology",
                        2.0),
                new BookWithAverageRatingDTO(3L, "Steve Jobs", "Walter Isaacson", 2011, "The biography of Apple " +
                        "co-founder Steve Jobs a",
                        "Biography", 0.0)
        );

    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(books);
        List<BookWithAverageRatingDTO> actualBooks = bookService.getAllBooks();

        Assertions.assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    public void testGetBooksByTitle() {

        String title = "Java";

        when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(Arrays.asList(books.get(0),
                books.get(1)));

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByTitle(title);

        Assertions.assertThat(actualBooks).isEqualTo(Arrays.asList(expectedBooks.get(0), expectedBooks.get(1)));
        Assertions.assertThat(actualBooks.get(0).getAverageRating()).isEqualTo(expectedBooks.get(0).getAverageRating());
        Assertions.assertThat(actualBooks.get(0).getTitle()).isEqualTo(expectedBooks.get(0).getTitle());
        Assertions.assertThat(actualBooks.get(0).getId()).isEqualTo(expectedBooks.get(0).getId());
    }

    @Test
    public void testGetBooksByAuthor() {

        String author = "brian";

        when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(Arrays.asList(books.get(0),
                books.get(1)));

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByAuthor(author);

        Assertions.assertThat(actualBooks).isEqualTo(Arrays.asList(expectedBooks.get(0), expectedBooks.get(1)));
        Assertions.assertThat(actualBooks).hasSize(Arrays.asList(expectedBooks.get(0), expectedBooks.get(1)).size());
    }

    @Test
    public void testGetBooksByGenre() {

        String genre = "biography";

        when(bookRepository.findByGenreIgnoreCase(genre)).thenReturn(Arrays.asList(books.get(2)));

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByGenre(genre);

        Assertions.assertThat(actualBooks).isEqualTo(Arrays.asList(expectedBooks.get(2)));
        Assertions.assertThat(actualBooks).hasSize(Arrays.asList(expectedBooks.get(2)).size());
    }

    @Test
    public void testGetBooksByDescriptionMultipleKeywords() {

        List<String> keywords = Arrays.asList("java", "a");

        when(bookRepository.findAll(any(Specification.class)))
                .thenReturn(Arrays.asList(books.get(0), books.get(1)));

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByWordsInDescription(keywords);

        Assertions.assertThat(actualBooks).isEqualTo(Arrays.asList(expectedBooks.get(0), expectedBooks.get(1)));
    }

    @Test
    public void testGetBooksByDescriptionOneKeyword() {

        List<String> keywords = Arrays.asList("a");

        when(bookRepository.findAll(any(Specification.class)))
                .thenReturn(books);

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByWordsInDescription(keywords);

        Assertions.assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    public void testGetBooksByYear() {

        Integer year = 2024;

        when(bookRepository.findByPublicationYear(year)).thenReturn(Arrays.asList(books.get(1)));

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByYear(year);

        Assertions.assertThat(actualBooks).isEqualTo(Arrays.asList(expectedBooks.get(1)));
        Assertions.assertThat(actualBooks.get(0).getPublicationYear()).isEqualTo(expectedBooks.get(1).getPublicationYear());
        Assertions.assertThat(actualBooks.get(0).getId()).isEqualTo(expectedBooks.get(1).getId());
    }

    @Test
    public void testGetBooksByRange() {

        double min = 3.0;
        double max = 5.0;


        when(bookRepository.findAll()).thenReturn(books);

        List<BookWithAverageRatingDTO> actualBooks = bookService.getBooksByRating(Optional.of(min), Optional.of(max));
        Assertions.assertThat(actualBooks).isEqualTo(Arrays.asList(expectedBooks.get(0)));

        List<BookWithAverageRatingDTO> noBooks = bookService.getBooksByRating(Optional.of(max), Optional.of(max));
        Assertions.assertThat(noBooks).isEqualTo(Arrays.asList());

        List<BookWithAverageRatingDTO> allBooks = bookService.getBooksByRating(Optional.empty(), Optional.of(max));
        Assertions.assertThat(allBooks).isEqualTo(expectedBooks);
    }

}
