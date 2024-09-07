package libraryMS.exception.exceptions;

import org.springframework.http.HttpStatus;

public class CustomFileNotFoundException extends ApiBaseException {
    public CustomFileNotFoundException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
