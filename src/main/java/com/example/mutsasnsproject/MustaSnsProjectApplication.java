package com.example.mutsasnsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MustaSnsProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(MustaSnsProjectApplication.class, args);
    }

}
