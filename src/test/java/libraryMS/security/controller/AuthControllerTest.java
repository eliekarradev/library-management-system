package libraryMS.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.domain.ApplicationUser;
import libraryMS.security.entity.AuthRequest;
import libraryMS.security.service.ApplicationUserDetails;
import libraryMS.security.service.JwtService;
import libraryMS.service.ApplicationUserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.naming.AuthenticationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplicationUserService applicationUserService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void authenticateAndGetTokenSuccess() throws Exception {
        // Mocked login request data
        AuthRequest authRequest = new AuthRequest("admin@gmail.com", "password");
        String jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTcyNTczNDMxMCwiZXhwIjoxNzU3MjcwMTE0fQ.JTPb_fWfAsQLqrSfiVXztxUdFQ5kWJbnv8HqDiOVwOGglESelfeimKXKS_S2cwVcz5rav3a5G98sV4Mb5UpfKA";

        // Mock user details
        ApplicationUserDetails userDetails = mock(ApplicationUserDetails.class);
        when(userDetails.getApplicationUser()).thenReturn(mock(ApplicationUser.class));

        // Mock authentication manager and services
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(mock(AuthenticationManager.class).authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(mock(ApplicationUserService.class).loadUserByUsername(anyString())).thenReturn(userDetails);
        when(mock(JwtService.class).generateToken(anyString())).thenReturn(jwtToken);

        // Perform POST request to /auth/login
        mock(MockMvc.class).perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.message").value("Logged In Successfully"))
                .andExpect((ResultMatcher) jsonPath("$.data.token").value(jwtToken));
    }

    @Test
    public void authenticateAndGetTokenFailure() throws Exception {
        // Mocked login request data
        AuthRequest authRequest = new AuthRequest("admin@gmail.com", "wrongpassword");

        // Mock authentication failure
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(AuthenticationException.class);

        // Perform POST request to /auth/login with wrong credentials
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect((ResultMatcher) jsonPath("$.message").value("Error in Login,Check your credentials and try again"));
    }
}