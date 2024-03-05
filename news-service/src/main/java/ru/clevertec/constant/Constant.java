package ru.clevertec.constant;

/**
 * Класс для хранения констант
 */
public class Constant {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss:SSS";

    public static final String LRU = "LRU";
    public static final String LFU = "LFU";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_JOURNALIST = "JOURNALIST";
    public static final String ROLE_SUBSCRIBER = "SUBSCRIBER";
    public static final String ROLE_UNKNOWN = "UNKNOWN";
    public static final String ANONYMOUS_USERNAME = "Anonymous User";

    public static final String SQL_FIND_NEWS_UUID_BY_COMMENT_UUID = """
            SELECT n.uuid
            FROM news n
            JOIN comments c
                ON n.id = c.news_id
            WHERE c.uuid = :commentUuid
            """;

    public static final String MEDIA_TYPE_JSON = "application/json";

    public static final String NEWS_UUID = "77fcd1b2-568a-441c-92ec-0253338cd5aa";
    public static final String COMMENT_UUID = "b655f85d-4838-40ca-a853-e93760babd29";

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

    public static final String JSON_NEWS_REQUEST = """
            {
              "title": "News Title",
              "text": "News Text"
            }
            """;

    public static final String JSON_NEWS_RESPONSE = """
            {
              "uuid": "77fcd1b2-568a-441c-92ec-0253338cd5aa",
              "title": "News Title",
              "text": "News Text",
              "author": "Author Name",
              "createDate": "2024-02-15T09:30:00:000",
              "updateDate": "2024-02-15T09:30:00:000"
            }
            """;

    public static final String JSON_NEWS_RESPONSE_WITH_COMMENT = """
            {
              "uuid": "77fcd1b2-568a-441c-92ec-0253338cd5aa",
              "title": "News Title",
              "text": "News Text",
              "author": "Author Name",
              "createDate": "2024-02-15T09:30:00:000",
              "updateDate": "2024-02-15T09:30:00:000",
              "comments": []
            }
            """;

    public static final String JSON_PAGE_NEWS_RESPONSE = """
            {
              "content": [
                {
                  "uuid": "77fcd1b2-568a-441c-92ec-0253338cd5aa",
                  "title": "News Title",
                  "text": "News Text",
                  "createDate": "2024-02-15T09:30:00:000",
                  "updateDate": "2024-02-15T09:30:00:000"
                }
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 10
              }
            }
            """;

    public static final String JSON_COMMENT_REQUEST = """
            {
              "text": "Comment Text",
              "newsUuid": "77fcd1b2-568a-441c-92ec-0253338cd5aa"
            }
             """;

    public static final String JSON_COMMENT_RESPONSE = """
            {
              "uuid": "b655f85d-4838-40ca-a853-e93760babd29",
              "text": "Comment Text",
              "username": "Username",
              "createDate": "2024-02-15T09:31:00:000",
              "updateDate": "2024-02-15T09:31:00:000",
              "newsUuid": "77fcd1b2-568a-441c-92ec-0253338cd5aa"
            }
            """;

    public static final String JSON_PAGE_COMMENT_RESPONSE = """
            {
              "content": [
                {
                  "uuid": "b655f85d-4838-40ca-a853-e93760babd29",
                  "text": "Comment Text",
                  "username": "Username",
                  "createDate": "2024-02-15T09:31:00:000",
                  "updateDate": "2024-02-15T09:31:00:000"
                }
              ],
              "pageable": {
                "pageNumber": 0,
                "pageSize": 10
              }
            }
            """;
}
