package ru.clevertec.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.annotation.Logging;
import ru.clevertec.controller.api.UserControllerApi;
import ru.clevertec.service.client.UserClient;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.util.TokenDataUtil;

/**
 * Контроллер для работы с регистрацией и аутентификацией пользователей (User)
 *
 * @see UserClient
 * @see UserControllerApi
 * @see TokenDataUtil
 */
@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerApi {

    private final UserClient userClient;
    private final TokenDataUtil tokenDataUtil;

    @PostMapping("/registration")
    public ResponseEntity<TokenResponse> registration(@RequestBody @Valid UserRegistrationRequest userRequest) {
        var tokenResponse = userClient.registration(userRequest);

        tokenDataUtil.provideUserInfoFromToken(tokenResponse);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/authentication")
    public ResponseEntity<TokenResponse> authentication(@RequestBody @Valid UserAuthenticationRequest userRequest) {
        var tokenResponse = userClient.authentication(userRequest);

        tokenDataUtil.provideUserInfoFromToken(tokenResponse);

        return ResponseEntity.ok(tokenResponse);
    }
}
