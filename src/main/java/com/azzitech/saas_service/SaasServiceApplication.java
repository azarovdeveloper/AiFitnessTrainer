package com.azzitech.saas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SaasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaasServiceApplication.class, args);
	}

}
