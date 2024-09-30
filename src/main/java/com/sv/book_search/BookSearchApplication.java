package com.sv.book_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.sv.book_search.entity"})
public class BookSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSearchApplication.class, args);
	}

}
