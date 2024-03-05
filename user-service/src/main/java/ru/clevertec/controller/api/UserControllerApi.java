package ru.clevertec.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import ru.clevertec.annotation.ApiExceptionResponse;
import ru.clevertec.constant.Constant;
import ru.clevertec.controller.UserController;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;

import static ru.clevertec.constant.Constant.JSON_TOKEN_RESPONSE;
import static ru.clevertec.constant.Constant.JSON_USER_AUTHENTICATION_REQUEST;
import static ru.clevertec.constant.Constant.JSON_USER_REGISTRATION_REQUEST;
import static ru.clevertec.constant.Constant.MEDIA_TYPE_JSON;

/**
 * Интерфейс для конфигурации SpringDoc OpenAPI
 *
 * @see UserController
 * @see Constant
 */
@Tag(name = "Users Controller", description = "Controller For Working With Users Registration And Authentication")
public interface UserControllerApi {

    @Operation(method = "POST", summary = "Registration", description = "New User Registration")
    @RequestBody(description = "Request Body", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_USER_REGISTRATION_REQUEST),
            schema = @Schema(implementation = UserRegistrationRequest.class)))
    @ApiResponse(responseCode = "200", description = "Successful Registration", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_TOKEN_RESPONSE),
            schema = @Schema(implementation = TokenResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<TokenResponse> registration(UserRegistrationRequest userRequest);

    @Operation(method = "POST", summary = "Authentication", description = "User Authentication")
    @RequestBody(description = "Request Body", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_USER_AUTHENTICATION_REQUEST),
            schema = @Schema(implementation = UserAuthenticationRequest.class)))
    @ApiResponse(responseCode = "200", description = "Successful Authentication", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_TOKEN_RESPONSE),
            schema = @Schema(implementation = TokenResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<TokenResponse> authentication(UserAuthenticationRequest userRequest);
}
