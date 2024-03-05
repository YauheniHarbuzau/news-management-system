package ru.clevertec.exception;

import ru.clevertec.constant.Constant;

import static ru.clevertec.constant.Constant.REGISTRATION_AUTHENTICATION_EXCEPTION_MESSAGE_FORMAT;

/**
 * Исключение, генерируемое при ошибке регистрации или аутентификации
 *
 * @see Constant
 */
public class RegistrationAuthenticationException extends RuntimeException {

    public RegistrationAuthenticationException(String message) {
        super(message);
    }

    public static RegistrationAuthenticationException of() {
        return new RegistrationAuthenticationException(REGISTRATION_AUTHENTICATION_EXCEPTION_MESSAGE_FORMAT);
    }
}
