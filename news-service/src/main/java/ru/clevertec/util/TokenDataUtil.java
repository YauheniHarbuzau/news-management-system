package ru.clevertec.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.constant.Constant;
import ru.clevertec.controller.UserController;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.service.impl.CommentServiceImpl;
import ru.clevertec.service.impl.NewsServiceImpl;

import static ru.clevertec.constant.Constant.ANONYMOUS_USERNAME;
import static ru.clevertec.constant.Constant.ROLE_ADMIN;
import static ru.clevertec.constant.Constant.ROLE_JOURNALIST;
import static ru.clevertec.constant.Constant.ROLE_SUBSCRIBER;
import static ru.clevertec.constant.Constant.ROLE_UNKNOWN;

/**
 * Класс, предоставляющий информацию об пользователе (User) и правах доступа из токена доступа
 *
 * @see NewsServiceImpl
 * @see CommentServiceImpl
 * @see UserController
 * @see Constant
 */
@Component
public class TokenDataUtil {

    @Value("${security.token.secret-key}")
    private String secretKey;

    @Getter
    private String username = ANONYMOUS_USERNAME;

    @Getter
    private String userRole = ROLE_UNKNOWN;

    public void provideUserInfoFromToken(TokenResponse tokenResponse) {
        var token = tokenResponse.token();
        this.username = getUsernameFromToken(token);
        this.userRole = getRoleFromToken(token);
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equals(userRole);
    }

    public boolean isJournalist() {
        return ROLE_JOURNALIST.equals(userRole);
    }

    public boolean isSubscriber() {
        return ROLE_SUBSCRIBER.equals(userRole);
    }

    private String getUsernameFromToken(String token) {
        return getDecoder(token).getSubject();
    }

    private String getRoleFromToken(String token) {
        return getDecoder(token).getClaim("role")
                .asString()
                .replaceAll("^\\[|\\]$", "");
    }

    private DecodedJWT getDecoder(String token) {
        var JwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        return JwtVerifier.verify(token);
    }
}
