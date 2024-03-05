package ru.clevertec.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.handler.GlobalExceptionHandler;

/**
 * Конфигурационный класс
 *
 * @see GlobalExceptionHandler
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.exception.handler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class ExceptionHandlerConfig {

    @PostConstruct
    void init() {
        log.info("ExceptionHandlerConfig initialized");
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
