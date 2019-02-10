package com.ohierro.subscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class PublicApplication {
	public static void main(String[] args) {
		SpringApplication.run(PublicApplication.class, args);
	}

}

