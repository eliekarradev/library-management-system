package libraryMS.security.controller;

import jakarta.validation.Valid;
import libraryMS.security.entity.AuthRequest;
import libraryMS.domain.ApplicationUser;
import libraryMS.security.service.ApplicationUserDetails;
import libraryMS.security.service.JwtService;
import libraryMS.service.ApplicationUserService;
import libraryMS.utils.model.ResponseObject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final ApplicationUserService applicationUserService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Transactional
    @PostMapping("/auth/login")
    public ResponseEntity<Object> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        authRequest.setEmail(authRequest.getEmail().toLowerCase());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            ApplicationUserDetails user = applicationUserService.loadUserByUsername(authRequest.getEmail());

            Map<String, Object> response = new HashMap<>();

            if (user.getApplicationUser().getRecordStatus() != -1 && !user.getApplicationUser().getIsDisabled()) {
                String token = jwtService.generateToken(authRequest.getEmail());
                response.put("user", user.getApplicationUser());
                response.put("token", token);

                return ResponseObject.SUCCESS_RESPONSE("Logged In Successfully", response);
            }
            return ResponseObject.FAILED_RESPONSE("your account is disabled", HttpStatus.UNAUTHORIZED);

        }
        return ResponseObject.FAILED_RESPONSE("Error in Login,Check your credentials and try again", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/auth/logout", method = RequestMethod.GET)
    public ResponseEntity<Object> logout() {
        return ResponseObject.SUCCESS_RESPONSE("Logged Out Successfully", null);
    }

}
