package ru.clevertec.controller.api;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.service.dto.response.CommentResponse;

import java.util.List;

/**
 * Класс, предоставляющий форму для контента с пагинацией
 *
 * @see CommentControllerApi
 */
public class PageCommentResponse extends PageImpl<CommentResponse> {

    public PageCommentResponse(List<CommentResponse> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
