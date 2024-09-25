package com.wales.book_socials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookSocialsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSocialsApplication.class, args);
	}

}
