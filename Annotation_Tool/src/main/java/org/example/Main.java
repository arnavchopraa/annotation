package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.database")
@EntityScan(basePackages = "org.example.models")
public class Main {
    /**
     * Initial main file
     *
     * @param args args to main
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}