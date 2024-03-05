package ru.clevertec.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.aspect.LoggingAspect;

/**
 * Конфигурационный класс
 *
 * @see LoggingAspect
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.logging",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
public class LoggingConfig {

    @PostConstruct
    void init() {
        log.info("LoggingConfig initialized");
    }

    @Bean
    public LoggingAspect newsServiceAspect() {
        return new LoggingAspect();
    }
}
