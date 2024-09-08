package libraryMS.exception.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public  class ApiBaseException extends RuntimeException {
    String message;
    HttpStatus status;

    public ApiBaseException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
