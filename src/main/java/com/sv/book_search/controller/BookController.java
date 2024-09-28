package com.sv.book_search.controller;

import com.sv.book_search.entity.Book;
import com.sv.book_search.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/books/")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/filterByTitle")
    public List<Book> filterBooksByTitle(@RequestParam String title) {
        return bookService.filterBooksByTitle(title);
    }
}
