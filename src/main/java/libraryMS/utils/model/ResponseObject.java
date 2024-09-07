package libraryMS.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObject implements Serializable {

    private String status;
    private String message;
    private Object model;
    private Object list;
    private Object errors;
    private Integer code;


    public static ResponseEntity<Object> SUCCESS_RESPONSE(String message) {
        return ResponseObject.SUCCESS_RESPONSE(message, null, HttpStatus.OK);
    }

    public static ResponseEntity<Object> SUCCESS_RESPONSE(String message, Object data) {
        return ResponseObject.SUCCESS_RESPONSE(message, data, HttpStatus.OK);
    }

    public static ResponseEntity<Object> SUCCESS_RESPONSE(String message, Object data, HttpStatus code) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setStatus("Success");
        responseObject.setMessage(message);
        if (data instanceof List<?>) {
            responseObject.setList(data);
        } else {
            responseObject.setModel(data);
        }
        responseObject.setCode(code.value());
        responseObject.setErrors(null);
        return new ResponseEntity<>(
                responseObject,
                code
        );
    }


    public static ResponseEntity<Object> FAILED_RESPONSE(String message) {
        return ResponseObject.FAILED_RESPONSE(message, HttpStatus.INTERNAL_SERVER_ERROR, null, null);
    }

    public static ResponseEntity<Object> FAILED_RESPONSE(String message, HttpStatus code) {
        return ResponseObject.FAILED_RESPONSE(message, code, null, null);
    }

    public static ResponseEntity<Object> FAILED_RESPONSE(String message, HttpStatus code, Object errors) {
        return ResponseObject.FAILED_RESPONSE(message, code, errors, null);
    }


    public static ResponseEntity<Object> FAILED_RESPONSE(String message, HttpStatus code, Object errors, Object data) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setStatus("Failed");
        responseObject.setMessage(message);
        responseObject.setModel(data);
        responseObject.setCode(code.value());
        responseObject.setErrors(errors);
        return new ResponseEntity<>(
                responseObject,
                code
        );
    }
}