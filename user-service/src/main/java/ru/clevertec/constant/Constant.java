package ru.clevertec.constant;

/**
 * Класс для хранения констант
 */
public class Constant {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss:SSS";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_JOURNALIST = "JOURNALIST";
    public static final String ROLE_SUBSCRIBER = "SUBSCRIBER";

    public static final String MEDIA_TYPE_JSON = "application/json";

    public static final String JSON_USER_REGISTRATION_REQUEST = """
            {
              "username": "Username",
              "password": "qwerty",
              "email": "email@gmail.com",
              "role": "ADMIN"
            }
            """;

    public static final String JSON_USER_AUTHENTICATION_REQUEST = """
            {
              "username": "Admin",
              "password": "qwerty"
            }
            """;

    public static final String JSON_TOKEN_RESPONSE = """
            {
              "token": "JWT Token"
            }
            """;
}
