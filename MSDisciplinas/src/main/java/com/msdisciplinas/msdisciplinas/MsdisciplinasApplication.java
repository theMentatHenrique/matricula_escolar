package com.msdisciplinas.msdisciplinas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class MsdisciplinasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsdisciplinasApplication.class, args);
	}

}
