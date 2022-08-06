package com.example.healthyshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthyShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthyShopApplication.class, args);
    }

}
