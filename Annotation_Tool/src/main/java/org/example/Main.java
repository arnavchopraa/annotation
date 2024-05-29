package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableJpaRepositories(basePackages = "org.example.database")
@ComponentScan(basePackages = { "org.example.*" })
@EntityScan(basePackages = "org.example.models")

public class Main {
    /**
     * Main method - Runs the spring application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}