package ru.clevertec.testdatautil;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.dao.entity.News;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_DATE_1;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_1;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_2;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_DATE_2;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;

/**
 * Класс для предоставления тестовых данных
 *
 * @see ConstantTestData
 * @see NewsTestData
 */
@Data
@Builder(setterPrefix = "with")
public class CommentTestData {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = COMMENT_TEST_UUID_1;

    @Builder.Default
    private String text = "Comment 1 Text";

    @Builder.Default
    private String username = "Subscriber";

    @Builder.Default
    private LocalDateTime createDate = COMMENT_TEST_DATE_1;

    @Builder.Default
    private LocalDateTime updateDate = COMMENT_TEST_DATE_1;

    @Builder.Default
    private News news = NewsTestData.builder().withUuid(NEWS_TEST_UUID_1).build().buildNews();

    @Builder.Default
    private UUID newsUuid = NEWS_TEST_UUID_1;

    public Comment buildComment() {
        return new Comment(id, uuid, text, username, createDate, updateDate, news);
    }

    public CommentRequest buildCommentRequest() {
        return new CommentRequest(text, newsUuid);
    }

    public CommentResponse buildCommentResponse() {
        return new CommentResponse(uuid, text, username, createDate, updateDate, newsUuid);
    }

    public List<Comment> buildCommentList() {
        var news1 = buildComment();
        var news2 = builder()
                .withId(2L)
                .withUuid(COMMENT_TEST_UUID_2)
                .withText("Comment 2 Text")
                .withUsername("Journalist")
                .withCreateDate(NEWS_TEST_DATE_2)
                .withUpdateDate(NEWS_TEST_DATE_2)
                .build().buildComment();

        return List.of(news1, news2);
    }

    public Page<Comment> buildCommentPage() {
        return new PageImpl<>(buildCommentList());
    }
}
