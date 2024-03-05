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
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.dao.entity.News;
import ru.clevertec.dao.repository.CommentRepository;
import ru.clevertec.dao.repository.NewsRepository;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.service.mapper.CommentMapper;
import ru.clevertec.service.mapper.CommentMapperImpl;
import ru.clevertec.testdatautil.CommentTestData;
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
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_1;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_2;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;

/**
 * Тестовый класс для {@link NewsServiceImpl}
 *
 * @see CommentTestData
 * @see ConstantTestData
 * @see NewsTestData
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private TokenDataUtil tokenDataUtil;

    @Spy
    private final CommentMapper commentMapper = new CommentMapperImpl();

    @Captor
    private ArgumentCaptor<Comment> commentCaptor;

    @Nested
    class GetByUuidTest {
        @Test
        void checkGetByUuidShouldReturnCorrectCommentResponse() {
            // given
            UUID uuid = COMMENT_TEST_UUID_1;

            Comment expectedComment = CommentTestData.builder()
                    .build().buildComment();
            CommentResponse expectedCommentResponse = CommentTestData.builder()
                    .build().buildCommentResponse();

            doReturn(Optional.of(expectedComment))
                    .when(commentRepository).findCommentByUuid(uuid);
            doReturn(Optional.of(NEWS_TEST_UUID_1))
                    .when(newsRepository).findNewsUuidByCommentUuid(COMMENT_TEST_UUID_1);

            // when
            CommentResponse actualCommentResponse = commentService.getByUuid(uuid);

            // then
            assertEquals(expectedCommentResponse, actualCommentResponse);
        }

        @Test
        void checkGetByUuidShouldThrowEntityNotFoundException() {
            // given
            UUID uuidFound = COMMENT_TEST_UUID_1;
            UUID uuidNotFound = COMMENT_TEST_UUID_2;

            Comment comment = CommentTestData.builder()
                    .withUuid(uuidFound)
                    .build().buildComment();
            CommentResponse commentResponse = CommentTestData.builder()
                    .withUuid(uuidFound)
                    .build().buildCommentResponse();

            doReturn(Optional.of(comment))
                    .when(commentRepository).findCommentByUuid(uuidFound);
            doReturn(Optional.of(NEWS_TEST_UUID_1))
                    .when(newsRepository).findNewsUuidByCommentUuid(COMMENT_TEST_UUID_1);

            // when, then
            assertAll(
                    () -> assertDoesNotThrow(() -> commentService.getByUuid(uuidFound)),
                    () -> assertThrows(EntityNotFoundException.class, () -> commentService.getByUuid(uuidNotFound))
            );
        }
    }

    @Nested
    class GetAllTest {
        @Test
        void checkGetAllShouldReturnCorrectPageOfCommentResponse() {
            // given
            Page<Comment> expectedCommentPage = CommentTestData.builder()
                    .build().buildCommentPage();
            int expectedPageSize = expectedCommentPage.getSize();

            doReturn(expectedCommentPage)
                    .when(commentRepository).findAll(any(PageRequest.class));

            // when
            Page<CommentResponse> actualCommentResponsePage = commentService.getAll(0, 10);

            // then
            verify(commentRepository, times(1)).findAll(any(PageRequest.class));
            assertThat(actualCommentResponsePage.getTotalElements()).isEqualTo(expectedPageSize);
        }

        @Test
        void checkGetAllShouldReturnEmptyPage() {
            // given
            Page<Comment> expectedCommentPage = new PageImpl<>(emptyList());

            doReturn(expectedCommentPage)
                    .when(commentRepository).findAll(any(PageRequest.class));

            // when
            Page<CommentResponse> actualCommentResponsePage = commentService.getAll(0, 10);

            // then
            verify(commentRepository, times(1)).findAll(any(PageRequest.class));
            assertThat(actualCommentResponsePage).isEmpty();
        }
    }

    @Nested
    class GetAllByNewsUuidTest {
        @Test
        void checkGetAllByNewsUuidShouldReturnCorrectPageOfCommentResponse() {
            // given
            UUID newsUuid = NEWS_TEST_UUID_1;

            Page<Comment> expectedCommentPage = CommentTestData.builder()
                    .build().buildCommentPage();
            int expectedPageSize = expectedCommentPage.getSize();

            doReturn(expectedCommentPage)
                    .when(commentRepository).findAllByNews_Uuid(any(PageRequest.class), eq(newsUuid));

            // when
            Page<CommentResponse> actualCommentResponsePage = commentService.getAllByNewsUuid(0, 10, newsUuid);

            // then
            verify(commentRepository, times(1)).findAllByNews_Uuid(any(PageRequest.class), eq(newsUuid));
            assertThat(actualCommentResponsePage.getTotalElements()).isEqualTo(expectedPageSize);
        }

        @Test
        void checkGetAllByNewsUuidShouldReturnEmptyPage() {
            // given
            UUID newsUuid = NEWS_TEST_UUID_1;

            Page<Comment> expectedCommentPage = new PageImpl<>(emptyList());

            doReturn(expectedCommentPage)
                    .when(commentRepository).findAllByNews_Uuid(any(PageRequest.class), eq(newsUuid));

            // when
            Page<CommentResponse> actualCommentResponsePage = commentService.getAllByNewsUuid(0, 10, newsUuid);

            // then
            verify(commentRepository, times(1)).findAllByNews_Uuid(any(PageRequest.class), eq(newsUuid));
            assertThat(actualCommentResponsePage).isEmpty();
        }
    }

    @Nested
    class CreateTest {
        @Test
        void checkCreateShouldFillInMissingFields() {
            // given
            String username = "Subscriber";

            CommentRequest commentToCreate = CommentTestData.builder()
                    .build().buildCommentRequest();
            Comment expectedComment = commentMapper.toComment(commentToCreate);
            expectedComment.setUsername(username);

            News expectedNews = NewsTestData.builder()
                    .build().buildNews();

            doReturn(username)
                    .when(tokenDataUtil).getUsername();
            doReturn(expectedComment)
                    .when(commentRepository).save(expectedComment);
            doReturn(Optional.of(expectedNews))
                    .when(newsRepository).findNewsByUuid(commentToCreate.newsUuid());

            // when
            commentService.create(commentToCreate);

            // then
            verify(commentRepository, times(1)).save(commentCaptor.capture());
            Comment actualComment = commentCaptor.getValue();
            assertAll(
                    () -> assertThat(actualComment.getId()).isEqualTo(expectedComment.getId()),
                    () -> assertThat(actualComment.getUsername()).isEqualTo(username)
            );
        }
    }

    @Nested
    class UpdateTest {
        @Test
        void checkUpdateShouldWorkCorrectly() {
            // given
            UUID commentUuid = COMMENT_TEST_UUID_2;
            String username = "Journalist";

            CommentRequest commentToUpdate = CommentTestData.builder()
                    .build().buildCommentRequest();
            Comment commentOldData = CommentTestData.builder()
                    .withUuid(commentUuid)
                    .build().buildComment();
            Comment expectedComment = CommentTestData.builder()
                    .withUuid(commentUuid)
                    .withText(commentToUpdate.text())
                    .withUsername(username)
                    .withNewsUuid(commentToUpdate.newsUuid())
                    .build().buildComment();

            News expectedNews = NewsTestData.builder()
                    .build().buildNews();

            doReturn(username)
                    .when(tokenDataUtil).getUsername();
            doReturn(Optional.of(commentOldData))
                    .when(commentRepository).findCommentByUuid(commentUuid);
            doReturn(expectedComment)
                    .when(commentRepository).save(expectedComment);
            doReturn(Optional.of(expectedNews))
                    .when(newsRepository).findNewsByUuid(commentToUpdate.newsUuid());

            // when
            commentService.update(commentUuid, commentToUpdate);

            // then
            verify(commentRepository, times(1)).save(commentCaptor.capture());
            Comment actualComment = commentCaptor.getValue();
            assertAll(
                    () -> assertThat(actualComment.getId()).isEqualTo(commentOldData.getId()),
                    () -> assertThat(actualComment.getText()).isEqualTo(commentToUpdate.text()),
                    () -> assertThat(actualComment.getCreateDate()).isEqualTo(commentOldData.getCreateDate())
            );
        }
    }

    @Nested
    class DeleteByUuidTest {
        @Test
        void checkDeleteByUuidShouldWorkCorrectly() {
            // given
            UUID commentUuid = COMMENT_TEST_UUID_1;

            doNothing()
                    .when(commentRepository).deleteCommentByUuid(commentUuid);

            // when
            commentService.deleteByUuid(commentUuid);

            // then
            verify(commentRepository, times(1)).deleteCommentByUuid(commentUuid);
            assertThat(commentRepository.findCommentByUuid(commentUuid)).isEmpty();
        }
    }
}
