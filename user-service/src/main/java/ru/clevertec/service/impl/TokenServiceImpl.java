package ru.clevertec.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.clevertec.service.TokenService;

import java.util.Date;

/**
 * Имплементация сервиса для работы с токенами доступа
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${security.token.secret-key}")
    private String secretKey;

    @Value("${security.token.expiration}")
    private int tokenExpiration;

    /**
     * Генерация токена доступа
     *
     * @param userDetails информация об User в формате UserDetails
     * @return токен доступа в формате String
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withClaim("role", String.valueOf(userDetails.getAuthorities()))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .sign(Algorithm.HMAC256(secretKey));
    }
}
