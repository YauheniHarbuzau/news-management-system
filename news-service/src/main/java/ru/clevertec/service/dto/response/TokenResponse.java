package ru.clevertec.service.dto.response;

import lombok.Builder;

import java.io.Serializable;

/**
 * DTO с предоставлением токена доступа
 */
public record TokenResponse(String token) implements Serializable {
    @Builder public TokenResponse {}
}
