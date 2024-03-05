package ru.clevertec.service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.clevertec.dao.entity.Comment;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO для {@link Comment}
 */
public record CommentRequest(

        @Size(min = 1, max = 500)
        String text,

        @NotNull
        UUID newsUuid
) implements Serializable {
}
