package com.libraryMS;

import libraryMS.Starter;
import libraryMS.domain.ApplicationUser;
import libraryMS.security.controller.AuthController;
import libraryMS.security.entity.AuthRequest;
import libraryMS.security.service.ApplicationUserDetails;
import libraryMS.security.service.JwtService;
import libraryMS.service.ApplicationUserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
public class UserControllerTest extends AbstractTest {


    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private AuthController authController;


    @Test
    public void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(applicationUserService).isNotNull();
    }


    @Test
    public void loginTestSuccessForAdmin() throws Exception {
        String exampleUserJson = "{\"email\":\"admin@gmail.com\",\"password\":\"12345678\"}";

        ApplicationUserDetails userDetails = mock(ApplicationUserDetails.class);
        when(userDetails.getApplicationUser()).thenReturn(mock(ApplicationUser.class));
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(mock(AuthenticationManager.class).authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(mock(ApplicationUserService.class).loadUserByUsername(anyString())).thenReturn(userDetails);
        when(mock(JwtService.class).generateToken(anyString())).thenReturn(anyString());


        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(exampleUserJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model.token").exists())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void loginTestFail() throws Exception {
        String exampleUserJson = "{\"email\":\"admin@gmail.com\",\"password\":\"123456789\"}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(exampleUserJson))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());

    }

    public String getTokenFromLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("admin@gmail.com");
        authRequest.setPassword("12345678");

        ResponseEntity response = authController.authenticateAndGetToken(authRequest);


//        MockHttpServletResponse response = response.get();
        return "";
    }

}