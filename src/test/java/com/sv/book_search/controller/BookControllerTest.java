package com.sv.book_search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sv.book_search.dto.BookWithAverageRatingDTO;
import com.sv.book_search.dto.RatingRequest;
import com.sv.book_search.entity.Rating;
import com.sv.book_search.service.BookService;
import com.sv.book_search.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private RatingService ratingService;

    @Test
    public void testGetAllBooks() throws Exception {

        List<BookWithAverageRatingDTO> books = Arrays.asList(
                new BookWithAverageRatingDTO(1L, "Book 1", "Author 1", 2010, "Description 1", "Fiction", 4.0),
                new BookWithAverageRatingDTO(2L, "Book 2", "Author 2", 2015, "Description 2", "Fiction", 3.5)
        );
        when(bookService.filterBooks(null, null, null, null, null, null, null)).thenReturn(books);

        mockMvc.perform(get("/api/books/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[0].averageRating").value(4.0));
    }

    @Test
    public void testFilterBooks() throws Exception {
        List<BookWithAverageRatingDTO> books = Arrays.asList(
                new BookWithAverageRatingDTO(1L, "Book 1", "Author 1", 2010, "Description 1", "Fiction", 4.0)
        );
        when(bookService.filterBooks("Book 1", null, null, null, null, null, null)).thenReturn(books);

        mockMvc.perform(get("/api/books/filter")
                        .param("title", "Book 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book 1"));
    }

    @Test
    public void testSaveRating() throws Exception {
        RatingRequest ratingRequest = new RatingRequest(5.0, "user@example.com");

        Rating rating = new Rating();
        rating.setRating(5.0);
        rating.setUsername("user@example.com");

        when(ratingService.saveRatingForBook(1L, 5.0, "user@example.com")).thenReturn(rating);

        mockMvc.perform(post("/api/books/1/ratings")
                        .content(new ObjectMapper().writeValueAsString(ratingRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5.0))
                .andExpect(jsonPath("$.username").value("user@example.com"));

    }
}
