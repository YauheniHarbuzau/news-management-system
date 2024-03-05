package ru.clevertec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Main-класс
 */
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication(scanBasePackages = "ru.clevertec.*")
public class NewsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(NewsServiceApp.class, args);
    }
}
