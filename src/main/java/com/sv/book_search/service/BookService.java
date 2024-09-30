package com.sv.book_search.service;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import com.sv.book_search.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookWithAverageRatingDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return booksToDTO(books);
    }

    public List<BookWithAverageRatingDTO> getBooksByTitle(String title) {

        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);

        return booksToDTO(books);

    }

    public List<BookWithAverageRatingDTO> getBooksByYear(Integer year) {

        List<Book> books = bookRepository.findByPublicationYear(year);

        return booksToDTO(books);

    }

    public List<BookWithAverageRatingDTO> getBooksByAuthor(String author) {

        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);

        return booksToDTO(books);

    }

    public List<BookWithAverageRatingDTO> getBooksByGenre(String genre) {

        List<Book> books = bookRepository.findByGenreIgnoreCase(genre);

        return booksToDTO(books);

    }

    public List<BookWithAverageRatingDTO> getBooksByWordsInDescription(List<String> keywords) {

        Specification<Book> spec = BookSpecifications.descriptionContainsAllKeywords(keywords);
        List<Book> books = bookRepository.findAll(spec);
        return booksToDTO(books);

    }

    public List<BookWithAverageRatingDTO> getBooksByRating(Optional<Double> minRatingOpt, Optional<Double> maxRatingOpt) {
        double minRating = minRatingOpt.orElse(0.0);
        double maxRating = maxRatingOpt.orElse(5.0);
        List<Book> books = bookRepository.findAll();
        List<BookWithAverageRatingDTO> allBooksDTO = booksToDTO(books);
        return allBooksDTO.stream()
                .filter(bookDTO -> bookDTO.getAverageRating() >= minRating && bookDTO.getAverageRating() <= maxRating)
                .collect(Collectors.toList());
    }

    private BookWithAverageRatingDTO getBookDTO(Book book) {
        return new BookWithAverageRatingDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getGenre(),
                book.getAverageRating()
        );
    }

    private List<BookWithAverageRatingDTO> booksToDTO(List<Book> books) {

        return books.stream().map(book -> {
            List<Rating> ratings = book.getRatings() != null ? book.getRatings() : Collections.emptyList();
            return getBookDTO(book);
        }).collect(Collectors.toList());
    }

}


