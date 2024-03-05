package ru.clevertec.service.mapper;

import org.junit.jupiter.api.Test;
import ru.clevertec.dao.entity.News;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.testdatautil.NewsTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;

/**
 * Тестовый класс для {@link NewsMapper}
 *
 * @see NewsTestData
 */
class NewsMapperTest {

    private final NewsMapper newsMapper = new NewsMapperImpl();

    @Test
    void checkToResponseShouldReturnCorrectNewsResponse() {
        // given
        News mappingNews = NewsTestData.builder()
                .build().buildNews();

        // when
        NewsResponse actualNewsResponse = newsMapper.toResponse(mappingNews);

        // than
        assertThat(actualNewsResponse)
                .hasFieldOrPropertyWithValue("uuid", mappingNews.getUuid())
                .hasFieldOrPropertyWithValue("title", mappingNews.getTitle())
                .hasFieldOrPropertyWithValue("text", mappingNews.getText())
                .hasFieldOrPropertyWithValue("author", mappingNews.getAuthor())
                .hasFieldOrPropertyWithValue("createDate", mappingNews.getCreateDate())
                .hasFieldOrPropertyWithValue("updateDate", mappingNews.getUpdateDate());
    }

    @Test
    void checkToResponseWithCommentShouldReturnCorrectNewsResponseWithComment() {
        // given
        News mappingNews = NewsTestData.builder()
                .build().buildNews();

        // when
        NewsResponseWithComment actualNewsResponseWithComment = newsMapper.toResponseWithComment(mappingNews);

        // than
        assertThat(actualNewsResponseWithComment)
                .hasFieldOrPropertyWithValue("uuid", mappingNews.getUuid())
                .hasFieldOrPropertyWithValue("title", mappingNews.getTitle())
                .hasFieldOrPropertyWithValue("text", mappingNews.getText())
                .hasFieldOrPropertyWithValue("author", mappingNews.getAuthor())
                .hasFieldOrPropertyWithValue("createDate", mappingNews.getCreateDate())
                .hasFieldOrPropertyWithValue("updateDate", mappingNews.getUpdateDate())
                .hasFieldOrPropertyWithValue("comments", mappingNews.getComments());
    }

    @Test
    void checkToNewsShouldReturnCorrectNews() {
        // given
        NewsRequest mappingNewsRequest = NewsTestData.builder()
                .build().buildNewsRequest();

        // when
        News actualNews = newsMapper.toNews(mappingNewsRequest);

        // than
        assertThat(actualNews)
                .hasFieldOrPropertyWithValue(News.Fields.id, isNull())
                .hasFieldOrPropertyWithValue(News.Fields.uuid, isNull())
                .hasFieldOrPropertyWithValue(News.Fields.title, mappingNewsRequest.title())
                .hasFieldOrPropertyWithValue(News.Fields.text, mappingNewsRequest.text())
                .hasFieldOrPropertyWithValue(News.Fields.author, isNull())
                .hasFieldOrPropertyWithValue(News.Fields.createDate, isNull())
                .hasFieldOrPropertyWithValue(News.Fields.updateDate, isNull())
                .hasFieldOrPropertyWithValue(News.Fields.comments, isNull());
    }

    @Test
    void checkUpdateNewsShouldReturnCorrectNews() {
        // given
        News mappingNews = NewsTestData.builder()
                .build().buildNews();
        NewsRequest mappingNewsRequest = NewsTestData.builder()
                .build().buildNewsRequest();

        // when
        News actualNews = newsMapper.updateNews(mappingNews, mappingNewsRequest);

        // than
        assertThat(actualNews)
                .hasFieldOrPropertyWithValue(News.Fields.id, mappingNews.getId())
                .hasFieldOrPropertyWithValue(News.Fields.uuid, mappingNews.getUuid())
                .hasFieldOrPropertyWithValue(News.Fields.title, mappingNewsRequest.title())
                .hasFieldOrPropertyWithValue(News.Fields.text, mappingNewsRequest.text())
                .hasFieldOrPropertyWithValue(News.Fields.author, mappingNews.getAuthor())
                .hasFieldOrPropertyWithValue(News.Fields.createDate, mappingNews.getCreateDate())
                .hasFieldOrPropertyWithValue(News.Fields.updateDate, mappingNews.getUpdateDate())
                .hasFieldOrPropertyWithValue(News.Fields.comments, mappingNews.getComments());
    }
}
