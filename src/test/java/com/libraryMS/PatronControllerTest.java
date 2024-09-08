package com.libraryMS;

import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.Starter;
import libraryMS.controller.BookController;
import libraryMS.controller.PatronController;
import libraryMS.domain.Book;
import libraryMS.domain.Patron;
import libraryMS.security.controller.AuthController;
import libraryMS.security.entity.AuthRequest;
import libraryMS.service.PatronService;
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
public class PatronControllerTest extends AbstractTest {

    @Autowired
    private PatronService patronService;

    @Autowired
    private PatronController patronController;

    @Autowired
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(patronController).isNotNull();
        assertThat(patronService).isNotNull();
    }


    @Test
    public void testGetAllPatrons() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(get("/patrons").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.data", IsCollectionWithSize.hasSize(Matchers.greaterThan(0))));
    }

    @Test
    public void testGetPatronById() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(get("/patrons/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.name", not(emptyOrNullString())));
    }

    @Test
    public void testAddPatron() throws Exception {
        String token = getTokenFromLogin();

        Patron newPatron = new Patron("Patron test","patron number: 89987645,patron email: patron@patron.com",null);

        mvc.perform(post("/patrons")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatron)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.name").value("Patron test"));
    }

    @Test
    public void testUpdatePatron() throws Exception {
        String token = getTokenFromLogin();

        Patron updatedPatron = new Patron("Patron test2","patron number: 899876425,patron email: patro2n@patron.com",null);

        mvc.perform(put("/patrons/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatron)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model.name").value("Patron test2"));
    }

    @Test
    public void testDeletePatron() throws Exception {
        String token = getTokenFromLogin();

        mvc.perform(delete("/patrons/1")
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
