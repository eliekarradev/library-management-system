package libraryMS.exception.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends ApiBaseException {
    public static final String messageKey = "unauthorized";


    public UnAuthorizedException() {
        super(messageKey, HttpStatus.UNAUTHORIZED);
    }
}
