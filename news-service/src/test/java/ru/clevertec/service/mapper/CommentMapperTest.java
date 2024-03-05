package ru.clevertec.service.mapper;

import org.junit.jupiter.api.Test;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.testdatautil.CommentTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;

/**
 * Тестовый класс для {@link CommentMapper}
 *
 * @see CommentTestData
 */
class CommentMapperTest {

    private final CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void checkToResponseShouldReturnCorrectCommentResponse() {
        // given
        Comment mappingComment = CommentTestData.builder()
                .build().buildComment();

        // when
        CommentResponse actualCommentResponse = commentMapper.toResponse(mappingComment);

        // than
        assertThat(actualCommentResponse)
                .hasFieldOrPropertyWithValue("uuid", mappingComment.getUuid())
                .hasFieldOrPropertyWithValue("text", mappingComment.getText())
                .hasFieldOrPropertyWithValue("username", mappingComment.getUsername())
                .hasFieldOrPropertyWithValue("createDate", mappingComment.getCreateDate())
                .hasFieldOrPropertyWithValue("updateDate", mappingComment.getUpdateDate())
                .hasFieldOrPropertyWithValue("newsUuid", isNull());
    }

    @Test
    void checkToCommentShouldReturnCorrectComment() {
        // given
        CommentRequest mappingCommentRequest = CommentTestData.builder()
                .build().buildCommentRequest();

        // when
        Comment actualComment = commentMapper.toComment(mappingCommentRequest);

        System.out.println(actualComment);

        // than
        assertThat(actualComment)
                .hasFieldOrPropertyWithValue(Comment.Fields.id, isNull())
                .hasFieldOrPropertyWithValue(Comment.Fields.uuid, isNull())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, mappingCommentRequest.text())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, isNull())
                .hasFieldOrPropertyWithValue(Comment.Fields.createDate, isNull())
                .hasFieldOrPropertyWithValue(Comment.Fields.updateDate, isNull())
                .hasFieldOrPropertyWithValue(Comment.Fields.news, isNull());
    }

    @Test
    void checkUpdateCommentShouldReturnCorrectComment() {
        // given
        Comment mappingComment = CommentTestData.builder()
                .build().buildComment();
        CommentRequest mappingCommentRequest = CommentTestData.builder()
                .build().buildCommentRequest();

        // when
        Comment actualComment = commentMapper.updateComment(mappingComment, mappingCommentRequest);

        // than
        assertThat(actualComment)
                .hasFieldOrPropertyWithValue(Comment.Fields.id, mappingComment.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.uuid, mappingComment.getUuid())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, mappingCommentRequest.text())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, mappingComment.getUsername())
                .hasFieldOrPropertyWithValue(Comment.Fields.createDate, mappingComment.getCreateDate())
                .hasFieldOrPropertyWithValue(Comment.Fields.updateDate, mappingComment.getUpdateDate());
        assertThat(actualComment.getNews()).isNotNull();
    }
}
