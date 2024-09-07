package libraryMS.exception;

import libraryMS.exception.exceptions.ApiBaseException;
import libraryMS.exception.exceptions.CustomClassNotFoundException;
import libraryMS.exception.exceptions.CustomFileNotFoundException;
import libraryMS.exception.exceptions.UnAuthorizedException;
import libraryMS.utils.model.ResponseObject;
import libraryMS.utils.service.MessageSourceService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.persistence.PersistenceException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * Handles exceptions thrown from controllers and other components.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private final MessageSourceService mss;

    //private final FileStorageService fileStorageService;

    /**
     * Constructor for ControllerAdvisor.
     *
     * @param mss The MessageSourceService for retrieving localized messages.
     */
    public ControllerAdvisor(MessageSourceService mss) {
        this.mss = mss;
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ex.printStackTrace();
        if (ex.getMessage().contains("Duplicate entry")) {
            return handleFailedResponse("Duplicate ISBN value. Please provide a unique ISBN.", HttpStatus.CONFLICT, null);
        }

        return handleFailedResponse("Database error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        ex.printStackTrace();
        return handleFailedResponse("Access Denied", HttpStatus.FORBIDDEN, null);
    }


    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignatureException(SignatureException ex, HttpHeaders headers,
                                                           HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        String message = ex.getMessage();
        ex.printStackTrace();
        return handleFailedResponse(message, httpStatus, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        String message = ex.getMessage();
        ex.printStackTrace();
        return handleFailedResponse(message, httpStatus, null);
    }

    /**
     * Handles PersistenceException thrown by data access layer.
     *
     * @param ex The PersistenceException that was thrown.
     * @return ResponseEntity with a failed response for persistence-related errors.
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex) {
        return handleFailedResponse(mss.getMessage("operation.failed"), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * Handles MethodArgumentNotValidException thrown when validation fails on method arguments.
     *
     * @param ex      The MethodArgumentNotValidException that was thrown.
     * @param headers The headers for the response.
     * @param status  The HTTP status for the response.
     * @param request The current request.
     * @return ResponseEntity with a failed response for validation errors.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return handleValidationException(ex.getBindingResult().getFieldErrors());
    }

    /**
     * Handles NoHandlerFoundException thrown when no handler is found for a request.
     *
     * @param ex      The NoHandlerFoundException that was thrown.
     * @param headers The headers for the response.
     * @param status  The HTTP status for the response.
     * @param request The current request.
     * @return ResponseEntity with a failed response indicating resource not found.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleFailedResponse(mss.getMessage("not.found"), HttpStatus.NOT_FOUND, null);
    }


    /**
     * Handles HttpMessageNotReadableException thrown when JSON request parsing fails.
     *
     * @param ex      The HttpMessageNotReadableException that was thrown.
     * @param headers The headers for the response.
     * @param status  The HTTP status for the response.
     * @param request The current request.
     * @return ResponseEntity with a failed response indicating incorrect JSON format.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleFailedResponse(mss.getMessage("json.wrong.format"), HttpStatus.BAD_REQUEST, null);
    }

    /**
     * Handles CustomClassNotFoundException thrown when a custom class is not found.
     *
     * @param e The CustomClassNotFoundException that was thrown.
     * @return ResponseEntity with a failed response containing the exception message.
     */
    @ExceptionHandler(CustomClassNotFoundException.class)
    protected ResponseEntity<Object> handleCustomClassNotFoundException(CustomClassNotFoundException e) {
        return handleFailedResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * Handles CustomFileNotFoundException thrown when a custom file is not found.
     *
     * @param e The CustomFileNotFoundException that was thrown.
     * @return ResponseEntity with a failed response containing the exception message.
     */
    @ExceptionHandler(CustomFileNotFoundException.class)
    protected ResponseEntity<Object> handleCustomFileNotFoundException(CustomFileNotFoundException e) {
        return handleFailedResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    /**
     * Handles any other internal exceptions not specifically handled by other methods.
     *
     * @param ex      The exception that was thrown.
     * @param body    The body of the response.
     * @param headers The headers for the response.
     * @param status  The HTTP status for the response.
     * @param request The current request.
     * @return ResponseEntity with a failed response containing the exception message.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleFailedResponse(ex.getMessage(), (HttpStatus) status, null);
    }


    /**
     * Handles BindException thrown when binding validation errors occur.
     * @param ex The BindException that was thrown.
     * @param headers The headers for the response.
     * @param status The HTTP status for the response.
     * @param request The current request.
     * @return ResponseEntity with a failed response for binding validation errors.
     */
    /*@Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationException(ex.getBindingResult().getFieldErrors());
    }*/

    /**
     * Handles validation errors from MethodArgumentNotValidException or BindException.
     *
     * @param fieldErrors The list of field errors from the validation.
     * @return ResponseEntity with a failed response for validation errors.
     */
    private ResponseEntity<Object> handleValidationException(List<FieldError> fieldErrors) {
        String message = mss.getMessage("bad.request");
        Map<String, List<String>> errors = fieldErrors.stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));
        return handleFailedResponse(message, HttpStatus.BAD_REQUEST, errors);
    }

    /**
     * Handles all exceptions not specifically handled by other methods.
     *
     * @param ex The exception that was thrown.
     * @return ResponseEntity with a failed response containing error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        ex.printStackTrace();
        if (ex instanceof ApiBaseException) {
            httpStatus = ((ApiBaseException) ex).getStatus();
            message = mss.getMessage(message);
        }
//        else if (ex instanceof AccessDeniedException) {
//            httpStatus = HttpStatus.FORBIDDEN;
//        }
        return handleFailedResponse(message, httpStatus, null);
    }


    /**
     * Constructs a failed response entity with the given message, HTTP status, and error details.
     * Deletes any uploaded files.
     *
     * @param message The error message.
     * @param code    The HTTP status code.
     * @param errors  The detailed error information.
     * @return ResponseEntity with a failed response.
     */
    private ResponseEntity<Object> handleFailedResponse(String message, HttpStatus code, Object errors) {
        //fileStorageService.deleteUploadedFiles();
        return ResponseObject.FAILED_RESPONSE(message, code, errors);
    }
}
