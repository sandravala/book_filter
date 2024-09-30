package com.sv.book_search.controller;

import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.dto.RatingRequest;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.service.BookService;
import com.sv.book_search.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/books/")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RatingService ratingService;

    @GetMapping("/all")
    public List<BookWithAverageRatingDTO> getAllBooks() {
        return bookService.filterBooks(null, null, null, null, null, null, null);
    }

    @GetMapping("/filter")
    public List<BookWithAverageRatingDTO> filterBooks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "min", required = false) Integer minRating,
            @RequestParam(value = "max", required = false) Integer maxRating,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "keyword", required = false) List<String> keywords) {

            return bookService.filterBooks(title, minRating, maxRating, year, author, genre, keywords);
    }

    @PostMapping("/{bookId}/ratings")
    public ResponseEntity<Rating> saveRating(@PathVariable Long bookId,
                             @RequestBody RatingRequest ratingRequest) {
        Rating savedRating = ratingService.saveRatingForBook(bookId, ratingRequest.getRatingValue(), ratingRequest.getUsername());

        return ResponseEntity.ok(savedRating);
    }


}
