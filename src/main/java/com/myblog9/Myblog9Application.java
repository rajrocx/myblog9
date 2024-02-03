package com.myblog9;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Myblog9Application {

	public static void main(String[] args) {
		SpringApplication.run(Myblog9Application.class, args);
	}
	@Bean
     public ModelMapper modelMapper(){
		return new ModelMapper();
	 }
}
