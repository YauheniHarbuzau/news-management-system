package ru.clevertec.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.dao.entity.News;
import ru.clevertec.dao.repository.NewsRepository;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.service.mapper.NewsMapper;
import ru.clevertec.service.mapper.NewsMapperImpl;
import ru.clevertec.testdatautil.ConstantTestData;
import ru.clevertec.testdatautil.NewsTestData;
import ru.clevertec.util.TokenDataUtil;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_2;

/**
 * Тестовый класс для {@link NewsServiceImpl}
 *
 * @see NewsTestData
 * @see ConstantTestData
 */
@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private TokenDataUtil tokenDataUtil;

    @Spy
    private final NewsMapper newsMapper = new NewsMapperImpl();

    @Captor
    private ArgumentCaptor<News> newsCaptor;

    @Nested
    class GetByUuidTest {
        @Test
        void checkGetByUuidShouldReturnCorrectNewsResponse() {
            // given
            UUID uuid = NEWS_TEST_UUID_1;

            News expectedNews = NewsTestData.builder()
                    .build().buildNews();
            NewsResponse expectedNewsResponse = NewsTestData.builder()
                    .build().buildNewsResponse();

            doReturn(Optional.of(expectedNews))
                    .when(newsRepository).findNewsByUuid(uuid);

            // when
            NewsResponse actualNewsResponse = newsService.getByUuid(uuid);

            // then
            assertEquals(expectedNewsResponse, actualNewsResponse);
        }

        @Test
        void checkGetByUuidShouldThrowEntityNotFoundException() {
            // given
            UUID uuidFound = NEWS_TEST_UUID_1;
            UUID uuidNotFound = NEWS_TEST_UUID_2;

            News news = NewsTestData.builder()
                    .withUuid(uuidFound)
                    .build().buildNews();
            NewsResponse newsResponse = NewsTestData.builder()
                    .withUuid(uuidFound)
                    .build().buildNewsResponse();

            doReturn(Optional.of(news))
                    .when(newsRepository).findNewsByUuid(uuidFound);

            // when, then
            assertAll(
                    () -> assertDoesNotThrow(() -> newsService.getByUuid(uuidFound)),
                    () -> assertThrows(EntityNotFoundException.class, () -> newsService.getByUuid(uuidNotFound))
            );
        }
    }

    @Nested
    class GetByUuidWithCommentTest {
        @Test
        void checkGetByUuidWithCommentShouldReturnCorrectNewsResponseWithComment() {
            // given
            UUID uuid = NEWS_TEST_UUID_1;

            News expectedNews = NewsTestData.builder()
                    .build().buildNews();
            NewsResponseWithComment expectedNewsResponseWithComment = NewsTestData.builder()
                    .build().buildNewsResponseWithComment();

            doReturn(Optional.of(expectedNews))
                    .when(newsRepository).findNewsByUuid(uuid);

            // when
            NewsResponseWithComment actualNewsResponseWithComment = newsService.getByUuidWithComment(uuid);

            // then
            assertEquals(expectedNewsResponseWithComment, actualNewsResponseWithComment);
        }

        @Test
        void checkGetByUuidWithCommentShouldThrowEntityNotFoundException() {
            // given
            UUID uuidFound = NEWS_TEST_UUID_1;
            UUID uuidNotFound = NEWS_TEST_UUID_2;

            News news = NewsTestData.builder()
                    .withUuid(uuidFound)
                    .build().buildNews();
            NewsResponseWithComment newsResponseWithComment = NewsTestData.builder()
                    .withUuid(uuidFound)
                    .build().buildNewsResponseWithComment();

            doReturn(Optional.of(news))
                    .when(newsRepository).findNewsByUuid(uuidFound);

            // when, then
            assertAll(
                    () -> assertDoesNotThrow(() -> newsService.getByUuidWithComment(uuidFound)),
                    () -> assertThrows(EntityNotFoundException.class, () -> newsService.getByUuidWithComment(uuidNotFound))
            );
        }
    }

    @Nested
    class GetAllTest {
        @Test
        void checkGetAllShouldReturnCorrectPageOfNewsResponse() {
            // given
            Page<News> expectedNewsPage = NewsTestData.builder()
                    .build().buildNewsPage();
            int expectedPageSize = expectedNewsPage.getSize();

            doReturn(expectedNewsPage)
                    .when(newsRepository).findAll(any(PageRequest.class));

            // when
            Page<NewsResponse> actualNewsResponsePage = newsService.getAll(0, 10);

            // then
            verify(newsRepository, times(1)).findAll(any(PageRequest.class));
            assertThat(actualNewsResponsePage.getTotalElements()).isEqualTo(expectedPageSize);
        }

        @Test
        void checkGetAllShouldReturnEmptyPage() {
            // given
            Page<News> expectedNewsPage = new PageImpl<>(emptyList());

            doReturn(expectedNewsPage)
                    .when(newsRepository).findAll(any(PageRequest.class));

            // when
            Page<NewsResponse> actualNewsResponsePage = newsService.getAll(0, 10);

            // then
            verify(newsRepository, times(1)).findAll(any(PageRequest.class));
            assertThat(actualNewsResponsePage).isEmpty();
        }
    }

    @Nested
    class SearchByTextTest {
        @Test
        void checkGetAllShouldReturnCorrectPageOfNewsResponse() {
            // given
            Page<News> expectedNewsPage = NewsTestData.builder()
                    .build().buildNewsPage();
            int expectedPageSize = expectedNewsPage.getSize();

            doReturn(expectedNewsPage)
                    .when(newsRepository).findAll(any(Specification.class), any(PageRequest.class));

            // when
            Page<NewsResponse> actualNewsResponsePage = newsService.getAll(0, 10, "News");

            // then
            verify(newsRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
            assertThat(actualNewsResponsePage.getTotalElements()).isEqualTo(expectedPageSize);
        }

        @Test
        void checkGetAllShouldReturnEmptyPage() {
            // given
            Page<News> expectedNewsPage = new PageImpl<>(emptyList());

            doReturn(expectedNewsPage)
                    .when(newsRepository).findAll(any(Specification.class), any(PageRequest.class));

            // when
            Page<NewsResponse> actualNewsResponsePage = newsService.getAll(0, 10, "News");

            // then
            verify(newsRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
            assertThat(actualNewsResponsePage).isEmpty();
        }
    }

    @Nested
    class CreateTest {
        @Test
        void checkCreateShouldFillInMissingFields() {
            // given
            NewsRequest newsToCreate = NewsTestData.builder()
                    .build().buildNewsRequest();
            News expectedNews = newsMapper.toNews(newsToCreate);

            doReturn("Journalist")
                    .when(tokenDataUtil).getUsername();

            // when
            newsService.create(newsToCreate);

            // then
            verify(newsRepository, times(1)).save(newsCaptor.capture());
            News actualNews = newsCaptor.getValue();
            assertAll(
                    () -> assertThat(actualNews.getId()).isEqualTo(expectedNews.getId()),
                    () -> assertThat(actualNews.getAuthor()).isEqualTo("Journalist")
            );
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void checkUpdateShouldWorkCorrectly() {
            // given
            UUID newsUuid = NEWS_TEST_UUID_2;

            NewsRequest newsToUpdate = NewsTestData.builder()
                    .build().buildNewsRequest();
            News newsOldData = NewsTestData.builder()
                    .withUuid(newsUuid)
                    .build().buildNews();

            doReturn(Optional.of(newsOldData))
                    .when(newsRepository).findNewsByUuid(newsUuid);

            // when
            newsService.update(newsUuid, newsToUpdate);

            // then
            verify(newsRepository, times(1)).save(newsCaptor.capture());
            News actualNews = newsCaptor.getValue();
            assertAll(
                    () -> assertThat(actualNews.getId()).isEqualTo(newsOldData.getId()),
                    () -> assertThat(actualNews.getTitle()).isEqualTo(newsToUpdate.title()),
                    () -> assertThat(actualNews.getText()).isEqualTo(newsToUpdate.text()),
                    () -> assertThat(actualNews.getCreateDate()).isEqualTo(newsOldData.getCreateDate())
            );
        }
    }

    @Nested
    class DeleteByUuidTest {
        @Test
        void checkDeleteByUuidShouldWorkCorrectly() {
            // given
            UUID newsUuid = NEWS_TEST_UUID_1;

            doNothing()
                    .when(newsRepository).deleteNewsByUuid(newsUuid);

            // when
            newsService.deleteByUuid(newsUuid);

            // then
            verify(newsRepository, times(1)).deleteNewsByUuid(newsUuid);
            assertThat(newsRepository.findNewsByUuid(newsUuid)).isEmpty();
        }
    }
}
