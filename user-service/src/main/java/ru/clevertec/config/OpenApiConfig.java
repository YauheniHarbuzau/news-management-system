package ru.clevertec.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Конфигурационный класс для SpringDoc OpenAPI
 */
@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(addInfo())
                .servers(addServers());
    }

    private Info addInfo() {
        return new Info()
                .title("User Registration And Authentication")
                .version("1.0")
                .description("Clevertec Courses Task")
                .contact(addContact());
    }

    private Contact addContact() {
        var contact = new Contact();
        contact.setName("Yauheni Harbuzau");
        contact.setUrl("https://github.com/YauheniHarbuzau");
        return contact;
    }

    private List<Server> addServers() {
        var server = new Server();
        server.setUrl("http://localhost:8081");
        return List.of(server);
    }
}
