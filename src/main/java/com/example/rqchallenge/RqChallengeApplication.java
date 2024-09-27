package com.example.rqchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan("com.example.rqchallenge.employees.model")
@ComponentScan(basePackages = {"com.example.rqchallenge"})
public class RqChallengeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RqChallengeApplication.class, args);
    }
}
