package it.discovery.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("it.discovery")
public class RestApplication {
	public static void main(String[] args) {
		SpringApplication.run(
				RestApplication.class, args);
	}
}
