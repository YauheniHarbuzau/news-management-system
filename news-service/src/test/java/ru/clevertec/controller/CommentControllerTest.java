package ru.clevertec.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.service.CommentService;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.testdatautil.CommentTestData;
import ru.clevertec.testdatautil.ConstantTestData;
import ru.clevertec.util.TokenDataUtil;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.testdatautil.ConstantTestData.COMMENT_TEST_UUID_1;

/**
 * Тестовый класс для {@link CommentController}
 *
 * @see CommentTestData
 * @see ConstantTestData
 */
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private TokenDataUtil tokenDataUtil;

    @Test
    void checkGetByUuidShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;
        CommentResponse commentResponse = CommentTestData.builder()
                .build().buildCommentResponse();

        doReturn(commentResponse)
                .when(commentService).getByUuid(uuid);

        // when, then
        mockMvc.perform(get("/api/v1/comments/{uuid}", uuid))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.uuid").value(uuid.toString()),
                        jsonPath("$.text").value("Comment 1 Text"),
                        jsonPath("$.username").value("Subscriber"),
                        jsonPath("$.createDate").value("2024-02-15T09:31:00:000"),
                        jsonPath("$.updateDate").value("2024-02-15T09:31:00:000")
                );
    }

    @Test
    void checkGetByUuidShouldThrowEntityNotFoundException_Status404() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        doThrow(EntityNotFoundException.class)
                .when(commentService).getByUuid(uuid);

        // when, then
        mockMvc.perform(get("/api/v1/comments/{uuid}", uuid))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkCreateShouldWorkCorrectly_Status201() throws Exception {
        // given
        CommentRequest commentToCreate = CommentTestData.builder()
                .build().buildCommentRequest();
        CommentResponse commentResponse = CommentTestData.builder()
                .build().buildCommentResponse();

        doReturn(true)
                .when(tokenDataUtil).isSubscriber();
        doReturn(commentResponse)
                .when(commentService).create(commentToCreate);

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
        CommentRequest commentToCreate = CommentTestData.builder()
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doThrow(EntityNotFoundException.class)
                .when(commentService).create(commentToCreate);

        // when, then
        mockMvc.perform(post("/api/v1/comments"))
                .andExpect(status().is4xxClientError())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    void checkUpdateShouldWorkCorrectly_Status200() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        CommentRequest commentToUpdate = CommentTestData.builder()
                .withText("Text To Update")
                .build().buildCommentRequest();
        CommentResponse commentResponse = CommentTestData.builder()
                .withText("Text To Update")
                .build().buildCommentResponse();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doReturn(commentResponse)
                .when(commentService).update(uuid, commentToUpdate);

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
    void checkUpdateShouldThrowEntityNotFoundException_Status4xx() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        CommentRequest commentToUpdate = CommentTestData.builder()
                .withText("Text To Update")
                .build().buildCommentRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doThrow(EntityNotFoundException.class)
                .when(commentService).update(uuid, commentToUpdate);

        // when, then
        mockMvc.perform(put("/api/v1/comments/{uuid}", uuid))
                .andExpect(status().is4xxClientError())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    void checkDeleteByUuidShouldWorkCorrectly_Status204() throws Exception {
        // given
        UUID uuid = COMMENT_TEST_UUID_1;

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doNothing()
                .when(commentService).deleteByUuid(uuid);

        // when, then
        mockMvc.perform(delete("/api/v1/comments/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }
}
