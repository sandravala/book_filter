package com.sv.book_search.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings", indexes = {
        @Index(name = "idx_ratings_book_id", columnList = "book_id"),
        @Index(name = "idx_ratings_rating", columnList = "rating")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rating;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    private Book book;

    private String username;
}
