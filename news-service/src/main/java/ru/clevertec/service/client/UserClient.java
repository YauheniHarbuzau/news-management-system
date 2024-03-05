package ru.clevertec.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;

/**
 * Клиент для работы с User
 */
@FeignClient(name = "UserClient", url = "${client.user-service.url}")
public interface UserClient {

    @PostMapping("/registration")
    TokenResponse registration(UserRegistrationRequest userRequest);

    @PostMapping("/authentication")
    TokenResponse authentication(UserAuthenticationRequest userRequest);
}
