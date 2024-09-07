package libraryMS.exception.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public  class ApiBaseException extends RuntimeException {
    String message;
    HttpStatus status;

    public ApiBaseException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
