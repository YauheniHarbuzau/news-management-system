package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.service.client.UserClient;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.testdatautil.ConstantTestData;
import ru.clevertec.util.TokenDataUtil;

import static org.mockito.Mockito.doReturn;
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
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @MockBean
    private TokenDataUtil tokenDataUtil;

    @Test
    void checkRegistrationShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UserRegistrationRequest userRequest = USER_REGISTRATION_REQUEST;
        TokenResponse tokenResponse = TOKEN_RESPONSE;

        doReturn(tokenResponse)
                .when(userClient).registration(userRequest);

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

        doReturn(tokenResponse)
                .when(userClient).authentication(userRequest);

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
