package com.sv.book_search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sv.book_search.BookSearchApplication;
import com.sv.book_search.dto.RatingRequest;
import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.repository.BookRepository;
import com.sv.book_search.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookSearchApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional // Ensures that each test method is executed in a transaction that will be rolled back after the test
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        ratingRepository.deleteAll();
        bookRepository.deleteAll();

        book1 = Book.builder()
                .title("Effective Java")
                .author("Joshua Bloch")
                .publicationYear(2008)
                .description("A comprehensive guide to Java programming.")
                .genre("Technology")
                .build();

        book2 = Book.builder()
                .title("Steve Jobs")
                .author("Walter Isaacson")
                .publicationYear(2011)
                .description("A biography of Steve Jobs.")
                .genre("Biography")
                .build();

        bookRepository.saveAll(Arrays.asList(book1, book2));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[1].title").value("Steve Jobs"));
    }

    @Test
    public void testFilterBooksByTitle() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("title", "Effective Java")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
    }

    @Test
    public void testSaveRating() throws Exception {
        RatingRequest ratingRequest = new RatingRequest(5.0, "user@example.com");

        mockMvc.perform(post("/api/books/" + book1.getId() + "/ratings")
                        .content(objectMapper.writeValueAsString(ratingRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5.0))
                .andExpect(jsonPath("$.username").value("user@example.com"));

        Rating savedRating = ratingRepository.findAll().get(0);
        assert Objects.equals(savedRating.getRating(), 5.0);
        assert savedRating.getUsername().equals("user@example.com");
    }
}
