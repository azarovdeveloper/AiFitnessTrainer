package com.azzitech.saas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AIServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIServiceApplication.class, args);
	}
}
