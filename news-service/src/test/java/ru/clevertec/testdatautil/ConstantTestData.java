package ru.clevertec.testdatautil;

import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.Month.FEBRUARY;

/**
 * Класс для предоставления тестовых констант
 */
public class ConstantTestData {

    public static final UUID NEWS_TEST_UUID_1 = UUID.fromString("77fcd1b2-568a-441c-92ec-0253338cd5aa");
    public static final UUID NEWS_TEST_UUID_2 = UUID.fromString("b6bed4a3-b963-46ff-8cc5-03c243a9cdfd");

    public static final UUID COMMENT_TEST_UUID_1 = UUID.fromString("b655f85d-4838-40ca-a853-e93760babd29");
    public static final UUID COMMENT_TEST_UUID_2 = UUID.fromString("31394f51-e141-40dc-9c57-324c067b0e33");

    public static final LocalDateTime NEWS_TEST_DATE_1 = LocalDateTime.of(2024, FEBRUARY, 15, 9, 30, 0);
    public static final LocalDateTime NEWS_TEST_DATE_2 = LocalDateTime.of(2024, FEBRUARY, 15, 9, 35, 0);

    public static final LocalDateTime COMMENT_TEST_DATE_1 = LocalDateTime.of(2024, FEBRUARY, 15, 9, 31, 0);
    public static final LocalDateTime COMMENT_TEST_DATE_2 = LocalDateTime.of(2024, FEBRUARY, 15, 9, 32, 0);

    public static final UserRegistrationRequest USER_REGISTRATION_REQUEST = new UserRegistrationRequest("Username", "qwerty", "user@gmail.com", "SUBSCRIBER");
    public static final UserAuthenticationRequest USER_AUTHENTICATION_REQUEST = new UserAuthenticationRequest("Username", "qwerty");
    public static final TokenResponse TOKEN_RESPONSE = new TokenResponse("JWT Token");
}
