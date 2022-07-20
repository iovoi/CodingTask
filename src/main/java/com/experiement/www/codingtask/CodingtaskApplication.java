package com.experiement.www.codingtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CodingtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingtaskApplication.class, args);
	}
}
