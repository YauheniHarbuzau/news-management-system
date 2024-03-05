package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.testdatautil.ConstantTestData;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.testdatautil.ConstantTestData.TOKEN_RESPONSE;
import static ru.clevertec.testdatautil.ConstantTestData.USER_AUTHENTICATION_REQUEST;
import static ru.clevertec.testdatautil.ConstantTestData.USER_REGISTRATION_REQUEST;

/**
 * Тестовый класс для {@link UserController}
 *
 * @see ConstantTestData
 */
@Disabled
@WireMockTest
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTestWireMock {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void checkRegistrationShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UserRegistrationRequest userRequest = USER_REGISTRATION_REQUEST;
        TokenResponse tokenResponse = TOKEN_RESPONSE;

        stubFor(WireMock.post(urlEqualTo("/api/v1/users/registration"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(userRequest)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(tokenResponse))
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
                )
        );

        // when, then
        mockMvc.perform(post("/api/v1/users/registration")
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.token").value(tokenResponse.token())
                );
    }

    @Test
    void checkAuthenticationShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UserAuthenticationRequest userRequest = USER_AUTHENTICATION_REQUEST;
        TokenResponse tokenResponse = TOKEN_RESPONSE;

        stubFor(WireMock.post(urlEqualTo("/api/v1/users/authentication"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(userRequest)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(tokenResponse))
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
                )
        );

        // when, then
        mockMvc.perform(post("/api/v1/users/authentication")
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.token").value(tokenResponse.token())
                );
    }
}
