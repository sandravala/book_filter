package com.sv.book_search.service;

import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import com.sv.book_search.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Rating> getRatingsForBook(Long bookId) {
        return ratingRepository.findByBookId(bookId);
    }

    public Rating saveRatingForBook(Long bookId, Double ratingValue, String username) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        Rating rating = Rating.builder()
                .book(book)
                .rating(ratingValue)
                .username(username)
                .build();

        return ratingRepository.save(rating);
    }
}
