package com.proyecto.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// CORS actualizado para permitir cualquier puerto localhost - v2
		SpringApplication.run(DemoApplication.class, args);
	}

}
