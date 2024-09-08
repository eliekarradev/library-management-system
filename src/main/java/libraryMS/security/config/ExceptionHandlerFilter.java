package libraryMS.security.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import libraryMS.utils.model.ResponseObject;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * To handle the errors that come from filters
 * An additional layer to handle errors
 */
@Component
@Primary
@Order(value = 1)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            handleResponse(request, response, HttpStatus.UNAUTHORIZED, "UnAuthorized");
        } catch (RuntimeException e) {
            e.printStackTrace();
            handleResponse(request, response, HttpStatus.INTERNAL_SERVER_ERROR, "Error");
        }
    }


    private void handleResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(
                ResponseObject.FAILED_RESPONSE(message, status)
        );
        response.getWriter().write(jsonResponse);
    }
}
