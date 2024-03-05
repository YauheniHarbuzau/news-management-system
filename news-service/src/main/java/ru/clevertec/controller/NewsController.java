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
import ru.clevertec.controller.api.NewsControllerApi;
import ru.clevertec.dao.entity.News;
import ru.clevertec.service.NewsService;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.service.impl.NewsServiceImpl;
import ru.clevertec.util.TokenDataUtil;

import java.util.UUID;

/**
 * Контроллер для работы с {@link News}
 *
 * @see NewsService
 * @see NewsServiceImpl
 * @see NewsControllerApi
 */
@Logging
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController implements NewsControllerApi {

    private final NewsService newsService;
    private final TokenDataUtil tokenDataUtil;

    @Override
    @GetMapping("/{uuid}")
    public ResponseEntity<NewsResponse> getByUuid(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(newsService.getByUuid(uuid));
    }

    @Override
    @GetMapping("/{uuid}/comment")
    public ResponseEntity<NewsResponseWithComment> getByUuidWithComment(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(newsService.getByUuidWithComment(uuid));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<NewsResponse>> getAll(@RequestParam(name = "pageNumber") int pageNumber,
                                                     @RequestParam(name = "pageMaxSize") int pageMaxSize) {
        return ResponseEntity.ok(newsService.getAll(pageNumber, pageMaxSize));
    }

    @Override
    @GetMapping("/searchbytext")
    public ResponseEntity<Page<NewsResponse>> searchByText(@RequestParam(name = "pageNumber") int pageNumber,
                                                           @RequestParam(name = "pageMaxSize") int pageMaxSize,
                                                           @RequestParam(name = "text") String text) {
        return ResponseEntity.ok(newsService.getAll(pageNumber, pageMaxSize, text));
    }

    @Override
    @PostMapping
    @PreAuthorize("@tokenDataUtil.isAdmin() || @tokenDataUtil.isJournalist()")
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid NewsRequest newsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsService.create(newsRequest));
    }

    @Override
    @PutMapping("/{uuid}")
    @PreAuthorize("@tokenDataUtil.isAdmin() || @tokenDataUtil.isJournalist()")
    public ResponseEntity<NewsResponse> update(@PathVariable("uuid") UUID uuid,
                                               @RequestBody @Valid NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.update(uuid, newsRequest));
    }

    @Override
    @DeleteMapping("/{uuid}")
    @PreAuthorize("@tokenDataUtil.isAdmin() || @tokenDataUtil.isJournalist()")
    public ResponseEntity<Void> deleteByUuid(@PathVariable("uuid") UUID uuid) {
        newsService.deleteByUuid(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
