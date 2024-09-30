package com.sv.book_search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookWithAverageRatingDTO {
    private Long id;
    private String title;
    private String author;
    private int publicationYear;
    private String description;
    private String genre;
    private double averageRating;
}
