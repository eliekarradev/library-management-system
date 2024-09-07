package libraryMS.exception.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiBaseException {
    public static final String messageKey = "not.found";

    public NotFoundException(String messageKey) {
        super(messageKey, HttpStatus.NOT_FOUND);
    }

    public NotFoundException() {
        super(messageKey, HttpStatus.NOT_FOUND);
    }
}
