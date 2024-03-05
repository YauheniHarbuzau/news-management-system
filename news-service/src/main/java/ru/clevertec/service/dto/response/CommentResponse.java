package ru.clevertec.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import ru.clevertec.constant.Constant;
import ru.clevertec.dao.entity.Comment;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.clevertec.constant.Constant.DATE_TIME_FORMAT;

/**
 * DTO для {@link Comment}
 *
 * @see Constant
 */
public record CommentResponse(

        UUID uuid,

        String text,

        String username,

        @JsonFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime createDate,

        @JsonFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime updateDate,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID newsUuid
) implements Serializable {
    @Builder public CommentResponse {}
}
