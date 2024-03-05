package ru.clevertec.testdatautil;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Класс для инициализации тест-контейнера
 */
@Transactional
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class PostgreSqlContainerInitializer {

    private static final String DOCKER_IMAGE_NAME = "postgres:15.2-alpine";
    private static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME);

    @BeforeAll
    public static void startContainer() {
        CONTAINER.start();
    }

    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add(SPRING_DATASOURCE_URL, CONTAINER::getJdbcUrl);
    }
}
