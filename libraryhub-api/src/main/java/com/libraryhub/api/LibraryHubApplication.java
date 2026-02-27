package com.libraryhub.api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.libraryhub")
@EnableJpaRepositories(basePackages = "com.libraryhub")
@EntityScan(basePackages = "com.libraryhub")
public class LibraryHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryHubApplication.class, args);
    }




}
