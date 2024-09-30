package com.sv.book_search.service;

import com.sv.book_search.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

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
}
