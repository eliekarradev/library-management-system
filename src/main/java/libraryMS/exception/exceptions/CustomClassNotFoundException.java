package libraryMS.exception.exceptions;

import org.springframework.http.HttpStatus;

public class CustomClassNotFoundException extends ApiBaseException {
    public CustomClassNotFoundException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

