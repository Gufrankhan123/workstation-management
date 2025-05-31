package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
	scanBasePackages = "com.example.demo",
	exclude = { SecurityAutoConfiguration.class }
)
@EntityScan("com.example.demo.model")
@EnableJpaRepositories("com.example.demo.repository")
public class WorkstationmanagementsysemApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkstationmanagementsysemApplication.class, args);
	}

}
