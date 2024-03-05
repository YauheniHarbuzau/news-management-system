package ru.clevertec.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import ru.clevertec.annotation.ApiExceptionResponse;
import ru.clevertec.constant.Constant;
import ru.clevertec.controller.NewsController;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;

import java.util.UUID;

import static ru.clevertec.constant.Constant.JSON_NEWS_REQUEST;
import static ru.clevertec.constant.Constant.JSON_NEWS_RESPONSE;
import static ru.clevertec.constant.Constant.JSON_NEWS_RESPONSE_WITH_COMMENT;
import static ru.clevertec.constant.Constant.JSON_PAGE_NEWS_RESPONSE;
import static ru.clevertec.constant.Constant.MEDIA_TYPE_JSON;
import static ru.clevertec.constant.Constant.NEWS_UUID;

/**
 * Интерфейс для конфигурации SpringDoc OpenAPI
 *
 * @see NewsController
 * @see Constant
 */
@Tag(name = "2. News Controller", description = "Controller For Working With News")
public interface NewsControllerApi {

    @Operation(method = "GET", summary = "Get News", description = "Get News By UUID")
    @ApiResponse(responseCode = "200", description = "News Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_NEWS_RESPONSE),
            schema = @Schema(implementation = NewsResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<NewsResponse> getByUuid(@Parameter(description = "News UUID", example = NEWS_UUID) UUID uuid);

    @Operation(method = "GET", summary = "Get News With Comment", description = "Get News With Comment By UUID")
    @ApiResponse(responseCode = "200", description = "News Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_NEWS_RESPONSE_WITH_COMMENT),
            schema = @Schema(implementation = NewsResponseWithComment.class)))
    @ApiExceptionResponse
    ResponseEntity<NewsResponseWithComment> getByUuidWithComment(@Parameter(description = "News UUID", example = NEWS_UUID) UUID uuid);

    @Operation(method = "GET", summary = "Get All News", description = "Get All News")
    @ApiResponse(responseCode = "200", description = "News Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_PAGE_NEWS_RESPONSE),
            schema = @Schema(implementation = PageNewsResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<Page<NewsResponse>> getAll(@Parameter(description = "Page Number", example = "0") int pageNumber,
                                              @Parameter(description = "Page Max Size", example = "10") int pageMaxSize);

    @Operation(method = "GET", summary = "Get All News", description = "News Search By Text")
    @ApiResponse(responseCode = "200", description = "News Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_PAGE_NEWS_RESPONSE),
            schema = @Schema(implementation = PageNewsResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<Page<NewsResponse>> searchByText(@Parameter(description = "Page Number", example = "0") int pageNumber,
                                                    @Parameter(description = "Page Max Size", example = "10") int pageMaxSize,
                                                    @Parameter(description = "Text For Search", example = "Text For Search") String text);

    @Operation(method = "POST", summary = "Create News", description = "Create New News")
    @RequestBody(description = "Request Body", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_NEWS_REQUEST),
            schema = @Schema(implementation = NewsRequest.class)))
    @ApiResponse(responseCode = "201", description = "News Created", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_NEWS_RESPONSE),
            schema = @Schema(implementation = NewsResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<NewsResponse> create(NewsRequest newsRequest);

    @Operation(method = "PUT", summary = "Update News", description = "Update Previous News")
    @RequestBody(description = "Request Body", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_NEWS_REQUEST),
            schema = @Schema(implementation = NewsRequest.class)))
    @ApiResponse(responseCode = "200", description = "News Updated", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_NEWS_RESPONSE),
            schema = @Schema(implementation = NewsResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<NewsResponse> update(@Parameter(description = "News UUID", example = NEWS_UUID) UUID uuid,
                                        NewsRequest newsRequest);

    @Operation(method = "DELETE", summary = "Delete News", description = "Delete News By UUID")
    @ApiResponse(responseCode = "204", description = "News Deleted")
    @ApiExceptionResponse
    ResponseEntity<Void> deleteByUuid(@Parameter(description = "News UUID", example = NEWS_UUID) UUID uuid);
}
