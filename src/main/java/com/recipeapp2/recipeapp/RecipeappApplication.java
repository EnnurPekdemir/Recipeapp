package com.recipeapp2.recipeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RecipeappApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecipeappApplication.class, args);
	}
}
