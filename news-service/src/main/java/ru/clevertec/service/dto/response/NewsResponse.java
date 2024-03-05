package ru.clevertec.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.clevertec.constant.Constant;
import ru.clevertec.dao.entity.News;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.clevertec.constant.Constant.DATE_TIME_FORMAT;

/**
 * DTO для {@link News}
 *
 * @see Constant
 */
public record NewsResponse(

        UUID uuid,

        String title,

        String text,

        String author,

        @JsonFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime createDate,

        @JsonFormat(pattern = DATE_TIME_FORMAT)
        LocalDateTime updateDate
) implements Serializable {
}
