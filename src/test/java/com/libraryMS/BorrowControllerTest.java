package com.libraryMS;

import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.Starter;
import libraryMS.controller.BookController;
import libraryMS.controller.BorrowController;
import libraryMS.domain.Book;
import libraryMS.security.controller.AuthController;
import libraryMS.security.entity.AuthRequest;
import libraryMS.service.BookService;
import libraryMS.service.BorrowService;
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

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
public class BorrowControllerTest extends AbstractTest {

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowController borrowController;

    @Autowired
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();




    @Test
    public void testReturnBook() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(put("/return/1/patron/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testBorrowBook() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(post("/borrow/1/patron/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(borrowController).isNotNull();
        assertThat(borrowService).isNotNull();
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
