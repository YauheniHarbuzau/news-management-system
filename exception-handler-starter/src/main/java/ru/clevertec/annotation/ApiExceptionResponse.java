package ru.clevertec.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ru.clevertec.constant.Constant;
import ru.clevertec.entity.ExceptionResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для предоставления исключений для SpringDoc OpenAPI
 *
 * @see Constant
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", content = @Content(
                mediaType = Constant.MEDIA_TYPE_JSON,
                examples = @ExampleObject(Constant.JSON_BAD_REQUEST),
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", content = @Content(
                mediaType = Constant.MEDIA_TYPE_JSON,
                examples = @ExampleObject(Constant.JSON_NOT_FOUND),
                schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "500", content = @Content(
                mediaType = Constant.MEDIA_TYPE_JSON,
                examples = @ExampleObject(Constant.JSON_INTERNAL_SERVER_ERROR),
                schema = @Schema(implementation = ExceptionResponse.class)))
})
public @interface ApiExceptionResponse {
}
