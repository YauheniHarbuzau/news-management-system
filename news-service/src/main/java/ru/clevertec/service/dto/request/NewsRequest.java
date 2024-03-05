package ru.clevertec.service.dto.request;

import jakarta.validation.constraints.Size;
import ru.clevertec.dao.entity.News;

import java.io.Serializable;

/**
 * DTO для {@link News}
 */
public record NewsRequest(

        @Size(min = 1, max = 100)
        String title,

        @Size(min = 1, max = 5000)
        String text
) implements Serializable {
}
