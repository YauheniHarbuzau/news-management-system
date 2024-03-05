package ru.clevertec.controller.api;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.service.dto.response.NewsResponse;

import java.util.List;

/**
 * Класс, предоставляющий форму для контента с пагинацией
 *
 * @see NewsControllerApi
 */
public class PageNewsResponse extends PageImpl<NewsResponse> {

    public PageNewsResponse(List<NewsResponse> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
