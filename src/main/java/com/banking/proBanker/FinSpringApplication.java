package com.banking.proBanker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class FinSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinSpringApplication.class, args);
	}

}
