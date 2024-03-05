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
import ru.clevertec.service.NewsService;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.testdatautil.ConstantTestData;
import ru.clevertec.testdatautil.NewsTestData;
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
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;

/**
 * Тестовый класс для {@link NewsController}
 *
 * @see NewsTestData
 * @see ConstantTestData
 */
@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsService newsService;

    @MockBean
    private TokenDataUtil tokenDataUtil;

    @Test
    void checkGetByUuidShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;
        NewsResponse newsResponse = NewsTestData.builder()
                .build().buildNewsResponse();

        doReturn(newsResponse)
                .when(newsService).getByUuid(uuid);

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}", uuid))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.uuid").value(uuid.toString()),
                        jsonPath("$.title").value("News 1 Title"),
                        jsonPath("$.text").value("News 1 Text"),
                        jsonPath("$.author").value("Journalist"),
                        jsonPath("$.createDate").value("2024-02-15T09:30:00:000"),
                        jsonPath("$.updateDate").value("2024-02-15T09:30:00:000")
                );
    }

    @Test
    void checkGetByUuidShouldThrowEntityNotFoundException_Status404() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        doThrow(EntityNotFoundException.class)
                .when(newsService).getByUuid(uuid);

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}", uuid))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkGetByUuidWithCommentShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;
        NewsResponseWithComment newsResponseWithComment = NewsTestData.builder()
                .build().buildNewsResponseWithComment();

        doReturn(newsResponseWithComment)
                .when(newsService).getByUuidWithComment(uuid);

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}/comment", uuid))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.uuid").value(uuid.toString()),
                        jsonPath("$.title").value("News 1 Title"),
                        jsonPath("$.text").value("News 1 Text"),
                        jsonPath("$.author").value("Journalist"),
                        jsonPath("$.createDate").value("2024-02-15T09:30:00:000"),
                        jsonPath("$.updateDate").value("2024-02-15T09:30:00:000")
                );
    }

    @Test
    void checkGetByUuidWithCommentShouldThrowEntityNotFoundException_Status404() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        doThrow(EntityNotFoundException.class)
                .when(newsService).getByUuidWithComment(uuid);

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}/comment", uuid))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkCreateShouldWorkCorrectly_Status201() throws Exception {
        // given
        NewsRequest newsToCreate = NewsTestData.builder()
                .build().buildNewsRequest();
        NewsResponse newsResponse = NewsTestData.builder()
                .build().buildNewsResponse();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doReturn(newsResponse)
                .when(newsService).create(newsToCreate);

        // when, then
        mockMvc.perform(post("/api/v1/news")
                .content(objectMapper.writeValueAsString(newsToCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.title").value(newsToCreate.title()),
                        jsonPath("$.text").value(newsToCreate.text()),
                        jsonPath("$.author").value("Journalist")
                );
    }

    @Test
    void checkCreateShouldThrowEntityNotFoundException_Status4xx() throws Exception {
        // given
        NewsRequest newsToCreate = NewsTestData.builder()
                .build().buildNewsRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doThrow(EntityNotFoundException.class)
                .when(newsService).create(newsToCreate);

        // when, then
        mockMvc.perform(post("/api/v1/news"))
                .andExpect(status().is4xxClientError())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    void checkUpdateShouldWorkCorrectly_Status200() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        NewsRequest newsToUpdate = NewsTestData.builder()
                .withText("Text To Update")
                .build().buildNewsRequest();
        NewsResponse newsResponse = NewsTestData.builder()
                .withText("Text To Update")
                .build().buildNewsResponse();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doReturn(newsResponse)
                .when(newsService).update(uuid, newsToUpdate);

        // when, then
        mockMvc.perform(put("/api/v1/news/{uuid}", uuid)
                .content(objectMapper.writeValueAsString(newsToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.text").value("Text To Update")
                );
    }

    @Test
    void checkUpdateShouldThrowEntityNotFoundException_Status4xx() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        NewsRequest newsToUpdate = NewsTestData.builder()
                .withText("Text To Update")
                .build().buildNewsRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doThrow(EntityNotFoundException.class)
                .when(newsService).update(uuid, newsToUpdate);

        // when, then
        mockMvc.perform(put("/api/v1/news/{uuid}", uuid))
                .andExpect(status().is4xxClientError())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass()
                        .equals(EntityNotFoundException.class));
    }

    @Test
    void checkDeleteByUuidShouldWorkCorrectly_Status204() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doNothing()
                .when(newsService).deleteByUuid(uuid);

        // when, then
        mockMvc.perform(delete("/api/v1/news/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }
}
