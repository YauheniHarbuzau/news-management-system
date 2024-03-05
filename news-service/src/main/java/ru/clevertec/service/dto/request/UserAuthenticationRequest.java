package ru.clevertec.service.dto.request;

import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO для выполнения запроса на аутентификацию пользователя
 */
public record UserAuthenticationRequest(

        @Size(min = 1, max = 100)
        String username,

        @Size(min = 1, max = 100)
        String password
) implements Serializable {
}
