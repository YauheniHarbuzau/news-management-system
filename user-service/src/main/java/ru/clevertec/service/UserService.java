package ru.clevertec.service;

import ru.clevertec.dao.entity.User;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.service.impl.UserServiceImpl;

/**
 * Интерфейс сервиса для работы с {@link User}
 *
 * @see UserServiceImpl
 */
public interface UserService {

    User getByUsername(String username);

    TokenResponse registration(UserRegistrationRequest userRegistrationRequest);

    TokenResponse authentication(UserAuthenticationRequest userAuthenticationRequest);
}
