package com.example.GoogleStarterProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class GoogleStarterProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleStarterProjectApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		return "Hello world!";
	}

}
