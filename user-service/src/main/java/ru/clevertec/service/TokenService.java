package ru.clevertec.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Интерфейс сервиса для работы с токенами доступа
 */
public interface TokenService {

    String generateToken(UserDetails userDetails);
}
