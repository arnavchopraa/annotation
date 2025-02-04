package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.example.*")
@ComponentScan(basePackages = { "org.example.*" })
@EntityScan("org.example.*")
//@ComponentScan(basePackages = { "org.example.*" })
public class Main extends SpringBootServletInitializer {
    /**
     * Main method - Runs the spring application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}