package com.stock.managing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ManagingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagingApplication.class, args);
    }

}
