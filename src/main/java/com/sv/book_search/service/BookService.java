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

    public List<BookWithAverageRatingDTO> filterBooks (String title, Integer minRating, Integer maxRating, Integer year, String author, String genre, List<String> keywords) {
        Specification<Book> spec = BookSpecifications.filterBooks(title, minRating, maxRating, year, author, genre, keywords);

        List<Book> books = bookRepository.findAll(spec);

        return booksToDTO(books);
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
            return getBookDTO(book);
        }).collect(Collectors.toList());
    }

}


