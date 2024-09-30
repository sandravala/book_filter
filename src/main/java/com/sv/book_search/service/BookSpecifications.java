package com.sv.book_search.service;

import com.sv.book_search.entity.Book;
import com.sv.book_search.entity.Rating;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class BookSpecifications {

    public static Specification<Book> descriptionContainsAllKeywords(List<String> keywords) {
        return (root, query, builder) -> {
            Predicate predicate = builder.conjunction();  // Start with 'true'

            for (String keyword : keywords) {
                predicate = builder.and(predicate, builder.like(
                        builder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"
                ));
            }

            return predicate;
        };
    }

    public static Specification<Book> filterBooks(
            String title, Integer minRating, Integer maxRating, Integer year, String author, String genre, List<String> keywords) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filters based on provided parameters
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("publicationYear"), year));
            }

            if (author != null && !author.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
            }

            if (genre != null && !genre.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("genre")), "%" + genre.toLowerCase() + "%"));
            }


            if (minRating != null || maxRating != null) {
                Join<Book, Rating> ratingsJoin = root.join("ratings", JoinType.LEFT);
                query.groupBy(root.get("id")); // Group by book ID to calculate the average rating

                Expression<Double> avgRating = criteriaBuilder.coalesce(criteriaBuilder.avg(ratingsJoin.get("rating")), 0.0);
                double min = minRating != null ? minRating.doubleValue() : 0.0;
                double max = maxRating != null ? maxRating.doubleValue() : 5.0;

                query.having(criteriaBuilder.between(avgRating, min, max));
            }


            if (keywords != null && !keywords.isEmpty()) {
                List<Predicate> keywordPredicates = new ArrayList<>();
                for (String keyword : keywords) {
                    keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"));
                }
                predicates.add(criteriaBuilder.and(keywordPredicates.toArray(new Predicate[0])));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
