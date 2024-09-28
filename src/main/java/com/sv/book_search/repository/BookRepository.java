package com.sv.book_search.repository;

import com.sv.book_search.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository  extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByPublicationYear(int year);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByRatingBetween(double minRating, double maxRating);

    List<Book> findByDescription(String description);

}
