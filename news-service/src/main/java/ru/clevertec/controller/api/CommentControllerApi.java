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
import ru.clevertec.controller.CommentController;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;

import java.util.UUID;

import static ru.clevertec.constant.Constant.COMMENT_UUID;
import static ru.clevertec.constant.Constant.JSON_COMMENT_REQUEST;
import static ru.clevertec.constant.Constant.JSON_COMMENT_RESPONSE;
import static ru.clevertec.constant.Constant.JSON_PAGE_COMMENT_RESPONSE;
import static ru.clevertec.constant.Constant.MEDIA_TYPE_JSON;
import static ru.clevertec.constant.Constant.NEWS_UUID;

/**
 * Интерфейс для конфигурации SpringDoc OpenAPI
 *
 * @see CommentController
 * @see Constant
 */
@Tag(name = "3. Comments Controller", description = "Controller For Working With Comments")
public interface CommentControllerApi {

    @Operation(method = "GET", summary = "Get Comment", description = "Get Comment By UUID")
    @ApiResponse(responseCode = "200", description = "Comment Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_COMMENT_RESPONSE),
            schema = @Schema(implementation = CommentResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<CommentResponse> getByUuid(@Parameter(description = "Comment UUID", example = COMMENT_UUID) UUID uuid);

    @Operation(method = "GET", summary = "Get All Comments", description = "Get All Comments")
    @ApiResponse(responseCode = "200", description = "Comments Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_PAGE_COMMENT_RESPONSE),
            schema = @Schema(implementation = PageCommentResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<Page<CommentResponse>> getAll(@Parameter(description = "Page Number", example = "0") int pageNumber,
                                                 @Parameter(description = "Page Max Size", example = "10") int pageMaxSize);

    @Operation(method = "GET", summary = "Get All Comments", description = "Get All Comments By News UUID")
    @ApiResponse(responseCode = "200", description = "Comments Found", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_PAGE_COMMENT_RESPONSE),
            schema = @Schema(implementation = PageCommentResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<Page<CommentResponse>> getAllByNewsUuid(@Parameter(description = "Page Number", example = "0") int pageNumber,
                                                           @Parameter(description = "Page Max Size", example = "10") int pageMaxSize,
                                                           @Parameter(description = "News UUID", example = NEWS_UUID) UUID newsUuid);

    @Operation(method = "POST", summary = "Create Comment", description = "Create New Comment")
    @RequestBody(description = "Request Body", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_COMMENT_REQUEST),
            schema = @Schema(implementation = CommentRequest.class)))
    @ApiResponse(responseCode = "201", description = "Comment Created", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_COMMENT_RESPONSE),
            schema = @Schema(implementation = CommentResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<CommentResponse> create(CommentRequest commentRequest);

    @Operation(method = "PUT", summary = "Update Comment", description = "Update Previous Comment")
    @RequestBody(description = "Request Body", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_COMMENT_REQUEST),
            schema = @Schema(implementation = CommentRequest.class)))
    @ApiResponse(responseCode = "200", description = "Comment Updated", content = @Content(
            mediaType = MEDIA_TYPE_JSON,
            examples = @ExampleObject(JSON_COMMENT_RESPONSE),
            schema = @Schema(implementation = CommentResponse.class)))
    @ApiExceptionResponse
    ResponseEntity<CommentResponse> update(@Parameter(description = "Comment UUID", example = COMMENT_UUID) UUID uuid,
                                           CommentRequest commentRequest);

    @Operation(method = "DELETE", summary = "Delete Comment", description = "Delete Comment By UUID")
    @ApiResponse(responseCode = "204", description = "Comment Deleted")
    @ApiExceptionResponse
    ResponseEntity<Void> deleteByUuid(@Parameter(description = "Comment UUID", example = COMMENT_UUID) UUID uuid);
}
