package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.testdatautil.CommentTestData;
import ru.clevertec.testdatautil.ConstantTestData;
import ru.clevertec.testdatautil.PostgreSqlContainerInitializer;
import ru.clevertec.util.TokenDataUtil;

import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_1;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_2;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;

/**
 * Тестовый класс для {@link CommentController}
 *
 * @see CommentTestData
 * @see ConstantTestData
 * @see PostgreSqlContainerInitializer
 */
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class CommentControllerTestIntegration extends PostgreSqlContainerInitializer {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private TokenDataUtil tokenDataUtil;

    @Test
    void checkGetByUuidShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        // when, then
        mockMvc.perform(get("/api/v1/comments/{uuid}", uuid))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.uuid").value(uuid.toString()),
                        jsonPath("$.text").value("Комментарий 1"),
                        jsonPath("$.username").value("Subscriber")
                );
    }

    @Test
    void checkGetByUuidShouldThrowEntityNotFoundException_Status404() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

        // when, then
        mockMvc.perform(get("/api/v1/comments/{uuid}", uuid))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkGetAllShouldShouldReturnCorrectResult_Status200() throws Exception {
        // given, when, then
        mockMvc.perform(get("/api/v1/comments")
                .param("pageNumber", "0")
                .param("pageMaxSize", "10"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("content").isArray(),
                        jsonPath("content[0].uuid").value(COMMENT_TEST_UUID_1.toString()),
                        jsonPath("content[0].text").value("Комментарий 1"),
                        jsonPath("content[0].username").value("Subscriber"),
                        jsonPath("content[1].uuid").value(COMMENT_TEST_UUID_2.toString()),
                        jsonPath("content[1].text").value("Комментарий 2"),
                        jsonPath("content[1].username").value("Subscriber")
                );
    }

    @Test
    void checkGetAllByNewsUuidShouldReturnCorrectResult_Status200() throws Exception {
        // given, when, then
        mockMvc.perform(get("/api/v1/comments/bynews")
                .param("pageNumber", "0")
                .param("pageMaxSize", "10")
                .param("newsUuid", NEWS_TEST_UUID_1.toString()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("content").isArray(),
                        jsonPath("content[0].uuid").value(COMMENT_TEST_UUID_1.toString()),
                        jsonPath("content[0].text").value("Комментарий 1"),
                        jsonPath("content[0].username").value("Subscriber"),
                        jsonPath("content[1].uuid").value(COMMENT_TEST_UUID_2.toString()),
                        jsonPath("content[1].text").value("Комментарий 2"),
                        jsonPath("content[1].username").value("Subscriber")
                );
    }

    @Test
    void checkGetAllByNewsUuidShouldReturnEmptyComments_Status200() throws Exception {
        // given
        String newsUuidNotFound = UUID.randomUUID().toString();

        // when, then
        mockMvc.perform(get("/api/v1/comments/bynews")
                .param("pageNumber", "0")
                .param("pageMaxSize", "10")
                .param("newsUuid", newsUuidNotFound))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("content").isArray(),
                        jsonPath("$.numberOfElements").value(0)
                );
    }

    @Test
    void checkCreateShouldWorkCorrectly_Status201() throws Exception {
        // given
        CommentRequest commentToCreate = CommentTestData.builder()
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();
        doReturn("Subscriber")
                .when(tokenDataUtil).getUsername();

        // when, then
        mockMvc.perform(post("/api/v1/comments")
                .content(objectMapper.writeValueAsString(commentToCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.text").value(commentToCreate.text()),
                        jsonPath("$.username").value("Subscriber")
                );
    }

    @Test
    void checkCreateShouldThrowEntityNotFoundException_Status4xx() throws Exception {
        // given
        UUID newsUuidNotFound = UUID.randomUUID();

        CommentRequest commentToCreate = CommentTestData.builder()
                .withNewsUuid(newsUuidNotFound)
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();

        // when, then
        mockMvc.perform(post("/api/v1/comments")
                .content(objectMapper.writeValueAsString(commentToCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is4xxClientError(),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkUpdateShouldWorkCorrectly_Status200() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        CommentRequest commentToUpdate = CommentTestData.builder()
                .withText("Text To Update")
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();
        doReturn("Subscriber")
                .when(tokenDataUtil).getUsername();

        // when, then
        mockMvc.perform(put("/api/v1/comments/{uuid}", uuid)
                .content(objectMapper.writeValueAsString(commentToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.text").value("Text To Update")
                );
    }

    @Test
    void checkUpdateShouldThrowEntityNotFoundException_1_Status4xx() throws Exception {
        // given
        UUID commentUuid = COMMENT_TEST_UUID_1;
        UUID newsUuidNotFound = UUID.randomUUID();

        CommentRequest commentToUpdate = CommentTestData.builder()
                .withText("Text To Update")
                .withNewsUuid(newsUuidNotFound)
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();

        // when, then
        mockMvc.perform(put("/api/v1/comments/{uuid}", commentUuid)
                .content(objectMapper.writeValueAsString(commentToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is4xxClientError(),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkUpdateShouldThrowEntityNotFoundException_2_Status4xx() throws Exception {
        // given
        UUID commentUuidNotFound = UUID.randomUUID();

        CommentRequest commentToUpdate = CommentTestData.builder()
                .withText("Text To Update")
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();

        // when, then
        mockMvc.perform(put("/api/v1/comments/{uuid}", commentUuidNotFound)
                .content(objectMapper.writeValueAsString(commentToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is4xxClientError(),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkDeleteByUuidShouldWorkCorrectly_Status204() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();

        // when, then
        mockMvc.perform(delete("/api/v1/comments/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }
}
