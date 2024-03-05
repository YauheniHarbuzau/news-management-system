package ru.clevertec.constant;

/**
 * Класс для хранения констант
 */
public class Constant {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss:SSS";

    public static final String ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT = "%s not found by: %s";
    public static final String PROVIDE_NEWS_BY_COMMENT_EXCEPTION_MESSAGE_FORMAT = "No News provided for comment with UUID: %s";
    public static final String CACHE_ALGORITHM_EXCEPTION_MESSAGE_FORMAT = "Unknown cache algorithm: %s";
    public static final String REGISTRATION_AUTHENTICATION_EXCEPTION_MESSAGE_FORMAT = "Registration/authentication failed";

    public static final String MEDIA_TYPE_JSON = "application/json";

    public static final String JSON_BAD_REQUEST = """
            {
              "timestamp": "2024-02-15T10:00:00:000",
              "status": 400,
              "message": "BAD_REQUEST",
              "debugMessage": "Debug Message"
            }
            """;

    public static final String JSON_NOT_FOUND = """
            {
              "timestamp": "2024-02-15T10:00:00:000",
              "status": 404,
              "message": "NOT_FOUND",
              "debugMessage": "Debug Message"
            }
            """;

    public static final String JSON_INTERNAL_SERVER_ERROR = """
            {
              "timestamp": "2024-02-15T10:00:00:000",
              "status": 500,
              "message": "INTERNAL_SERVER_ERROR",
              "debugMessage": "Debug Message"
            }
            """;
}
