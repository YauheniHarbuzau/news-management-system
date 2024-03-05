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
import ru.clevertec.dao.entity.User;
import ru.clevertec.service.UserService;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.service.impl.UserServiceImpl;

/**
 * Контроллер для работы с {@link User}
 *
 * @see UserService
 * @see UserServiceImpl
 * @see UserControllerApi
 */
@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerApi {

    private final UserService userService;

    @Override
    @PostMapping("/registration")
    public ResponseEntity<TokenResponse> registration(@RequestBody @Valid UserRegistrationRequest userRequest) {
        return ResponseEntity.ok(userService.registration(userRequest));
    }

    @Override
    @PostMapping("/authentication")
    public ResponseEntity<TokenResponse> authentication(@RequestBody @Valid UserAuthenticationRequest userRequest) {
        return ResponseEntity.ok(userService.authentication(userRequest));
    }
}
