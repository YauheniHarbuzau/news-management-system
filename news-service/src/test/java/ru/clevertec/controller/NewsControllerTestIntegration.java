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
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.testdatautil.ConstantTestData;
import ru.clevertec.testdatautil.NewsTestData;
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
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_1;
import static ru.clevertec.testdatautil.ConstantTestData.NEWS_TEST_UUID_2;

/**
 * Тестовый класс для {@link NewsController}
 *
 * @see NewsTestData
 * @see ConstantTestData
 * @see PostgreSqlContainerInitializer
 */
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class NewsControllerTestIntegration extends PostgreSqlContainerInitializer {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private TokenDataUtil tokenDataUtil;

    @Test
    void checkGetByUuidShouldReturnCorrectResult_Status200() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}", uuid))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.uuid").value(uuid.toString()),
                        jsonPath("$.title").value("Британские ученые..."),
                        jsonPath("$.author").value("Journalist")
                );
    }

    @Test
    void checkGetByUuidShouldThrowEntityNotFoundException_Status404() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

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

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}/comment", uuid))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.uuid").value(uuid.toString()),
                        jsonPath("$.title").value("Британские ученые..."),
                        jsonPath("$.author").value("Journalist"),
                        jsonPath("$.comments").isArray()
                );
    }

    @Test
    void checkGetByUuidWithCommentShouldThrowEntityNotFoundException_Status404() throws Exception {
        // given
        UUID uuid = UUID.randomUUID();

        // when, then
        mockMvc.perform(get("/api/v1/news/{uuid}/comment", uuid))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkGetAllShouldShouldReturnCorrectResult_Status200() throws Exception {
        // given, when, then
        mockMvc.perform(get("/api/v1/news")
                .param("pageNumber", "0")
                .param("pageMaxSize", "10"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("content").isArray(),
                        jsonPath("content[0].uuid").value(NEWS_TEST_UUID_1.toString()),
                        jsonPath("content[0].title").value("Британские ученые..."),
                        jsonPath("content[0].author").value("Journalist"),
                        jsonPath("content[1].uuid").value(NEWS_TEST_UUID_2.toString()),
                        jsonPath("content[1].title").value("Археологи в недоумении"),
                        jsonPath("content[1].author").value("Journalist")
                );
    }

    @Test
    void checkSearchByTextShouldShouldReturnCorrectResult_Status200() throws Exception {
        // given, when, then
        mockMvc.perform(get("/api/v1/news/searchbytext")
                .param("pageNumber", "0")
                .param("pageMaxSize", "10")
                .param("text", "Британские ученые"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("content").isArray(),
                        jsonPath("content[0].uuid").value(NEWS_TEST_UUID_1.toString()),
                        jsonPath("content[0].title").value("Британские ученые..."),
                        jsonPath("content[0].author").value("Journalist")
                );
    }

    @Test
    void checkCreateShouldWorkCorrectly_Status201() throws Exception {
        // given
        NewsRequest newsToCreate = NewsTestData.builder()
                .build().buildNewsRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doReturn("Journalist")
                .when(tokenDataUtil).getUsername();

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
    void checkUpdateShouldWorkCorrectly_Status200() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        NewsRequest newsToUpdate = NewsTestData.builder()
                .withText("Text To Update")
                .build().buildNewsRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();
        doReturn("Journalist")
                .when(tokenDataUtil).getUsername();

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
        String newsUuidNotFound = UUID.randomUUID().toString();

        NewsRequest newsToUpdate = NewsTestData.builder()
                .withText("Text To Update")
                .build().buildNewsRequest();

        doReturn(true)
                .when(tokenDataUtil).isJournalist();

        // when, then
        mockMvc.perform(put("/api/v1/news/{uuid}", newsUuidNotFound)
                .content(objectMapper.writeValueAsString(newsToUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is4xxClientError(),
                        mvcResult -> mvcResult.getResolvedException().getClass().equals(EntityNotFoundException.class)
                );
    }

    @Test
    void checkDeleteByUuidShouldWorkCorrectly_Status204() throws Exception {
        // given
        UUID uuid = NEWS_TEST_UUID_1;

        doReturn(true)
                .when(tokenDataUtil).isJournalist();

        // when, then
        mockMvc.perform(delete("/api/v1/news/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }
}
