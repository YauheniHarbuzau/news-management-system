package ru.clevertec.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.annotation.Logging;
import ru.clevertec.controller.api.CommentControllerApi;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.service.CommentService;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.service.impl.CommentServiceImpl;

import java.util.UUID;

/**
 * Контроллер для работы с {@link Comment}
 *
 * @see CommentService
 * @see CommentServiceImpl
 * @see CommentControllerApi
 */
@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController implements CommentControllerApi {

    private final CommentService commentService;

    @Override
    @GetMapping("/{uuid}")
    public ResponseEntity<CommentResponse> getByUuid(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(commentService.getByUuid(uuid));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<CommentResponse>> getAll(@RequestParam(name = "pageNumber") int pageNumber,
                                                        @RequestParam(name = "pageMaxSize") int pageMaxSize) {
        return ResponseEntity.ok(commentService.getAll(pageNumber, pageMaxSize));
    }

    @Override
    @GetMapping("/bynews")
    public ResponseEntity<Page<CommentResponse>> getAllByNewsUuid(@RequestParam(name = "pageNumber") int pageNumber,
                                                                  @RequestParam(name = "pageMaxSize") int pageMaxSize,
                                                                  @RequestParam(name = "newsUuid") UUID newsUuid) {
        return ResponseEntity.ok(commentService.getAllByNewsUuid(pageNumber, pageMaxSize, newsUuid));
    }

    @Override
    @PostMapping
    @PreAuthorize("@tokenDataUtil.isAdmin() || @tokenDataUtil.isJournalist() || @tokenDataUtil.isSubscriber()")
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.create(commentRequest));
    }

    @Override
    @PutMapping("/{uuid}")
    @PreAuthorize("@tokenDataUtil.isAdmin() || @tokenDataUtil.isJournalist() || @tokenDataUtil.isSubscriber()")
    public ResponseEntity<CommentResponse> update(@PathVariable("uuid") UUID uuid,
                                                  @RequestBody @Valid CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.update(uuid, commentRequest));
    }

    @Override
    @DeleteMapping("/{uuid}")
    @PreAuthorize("@tokenDataUtil.isAdmin() || @tokenDataUtil.isJournalist() || @tokenDataUtil.isSubscriber()")
    public ResponseEntity<Void> deleteByUuid(@PathVariable("uuid") UUID uuid) {
        commentService.deleteByUuid(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
