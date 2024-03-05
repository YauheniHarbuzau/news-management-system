package ru.clevertec.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.clevertec.constant.Constant;
import ru.clevertec.dao.entity.User;

import java.io.Serializable;

import static ru.clevertec.constant.Constant.ROLE_ADMIN;
import static ru.clevertec.constant.Constant.ROLE_JOURNALIST;
import static ru.clevertec.constant.Constant.ROLE_SUBSCRIBER;

/**
 * DTO для выполнения запроса на регистрацию пользователя
 *
 * @see User
 * @see Constant
 */
public record UserRegistrationRequest(

        @Size(min = 1, max = 100)
        String username,

        @Size(min = 1, max = 100)
        String password,

        @Email
        String email,

        @Pattern(regexp = "^" + ROLE_ADMIN + "|" + ROLE_JOURNALIST + "|" + ROLE_SUBSCRIBER + "$")
        String role
) implements Serializable {
}
