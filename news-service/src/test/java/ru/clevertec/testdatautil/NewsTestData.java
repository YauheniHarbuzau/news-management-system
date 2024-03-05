package ru.clevertec.testdatautil;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.dao.entity.News;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_DATE_1;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_DATE_2;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_2;

/**
 * Класс для предоставления тестовых данных
 *
 * @see ConstantTestData
 */
@Data
@Builder(setterPrefix = "with")
public class NewsTestData {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = NEWS_TEST_UUID_1;

    @Builder.Default
    private String title = "News 1 Title";

    @Builder.Default
    private String text = "News 1 Text";

    @Builder.Default
    private String author = "Journalist";

    @Builder.Default
    private LocalDateTime createDate = NEWS_TEST_DATE_1;

    @Builder.Default
    private LocalDateTime updateDate = NEWS_TEST_DATE_1;

    @Builder.Default
    private List<Comment> commentList = emptyList();

    @Builder.Default
    private List<CommentResponse> commentResponseList = emptyList();

    public News buildNews() {
        return new News(id, uuid, title, text, author, createDate, updateDate, commentList);
    }

    public NewsRequest buildNewsRequest() {
        return new NewsRequest(title, text);
    }

    public NewsResponse buildNewsResponse() {
        return new NewsResponse(uuid, title, text, author, createDate, updateDate);
    }

    public NewsResponseWithComment buildNewsResponseWithComment() {
        return new NewsResponseWithComment(uuid, title, text, author, createDate, updateDate, commentResponseList);
    }

    public List<News> buildNewsList() {
        var news1 = buildNews();
        var news2 = builder()
                .withId(2L)
                .withUuid(NEWS_TEST_UUID_2)
                .withTitle("News 2 Title")
                .withText("News 2 Text")
                .withAuthor("Journalist")
                .withCreateDate(NEWS_TEST_DATE_2)
                .withUpdateDate(NEWS_TEST_DATE_2)
                .build().buildNews();

        return List.of(news1, news2);
    }

    public Page<News> buildNewsPage() {
        return new PageImpl<>(buildNewsList());
    }
}
