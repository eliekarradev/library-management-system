package com.libraryMS;

import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.Starter;
import libraryMS.controller.BookController;
import libraryMS.domain.Book;
import libraryMS.security.controller.AuthController;
import libraryMS.security.entity.AuthRequest;
import libraryMS.service.BookService;
import libraryMS.utils.model.ResponseObject;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
public class BookControllerTest extends AbstractTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookController bookController;

    @Autowired
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(bookController).isNotNull();
        assertThat(bookService).isNotNull();
    }


    @Test
    public void testGetAllBooks() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(get("/books").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.data", IsCollectionWithSize.hasSize(Matchers.greaterThan(0))));
    }

    @Test
    public void testGetBookById() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(get("/books/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.title", not(emptyOrNullString())));
    }

    @Test
    public void testAddBook() throws Exception {
        String token = getTokenFromLogin();

        Book newBook = new Book("Hunger games2", "John week2", 2000, UUID.randomUUID().toString().replace("-", "").substring(0, 13), null);

        mvc.perform(post("/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.title").value("Hunger games2"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        String token = getTokenFromLogin();

        Book updatedBook = new Book("Updated Book", "Updated Author", 2022, "1d34d67891234", null);

        mvc.perform(put("/books/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.title").value("Updated Book"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(delete("/books/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public String getTokenFromLogin() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("admin@gmail.com");
        authRequest.setPassword("12345678");

        ResponseEntity<Object> response = authController.authenticateAndGetToken(authRequest);

        ResponseObject result = (ResponseObject) response.getBody();

        return (String) ((Map<String, Object>) result.getModel()).get("token");
    }
}
