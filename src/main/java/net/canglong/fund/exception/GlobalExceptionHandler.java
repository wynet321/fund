package net.canglong.fund.exception;

import net.canglong.fund.entity.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ResponseObject> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    ResponseObject body = new ResponseObject();
    body.setSuccess(false);
    body.setMessage("Invalid parameter: " + ex.getName());
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseObject> handleValidation(MethodArgumentNotValidException ex) {
    ResponseObject body = new ResponseObject();
    body.setSuccess(false);
    body.setMessage("Validation failed");
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseObject> handleIllegalArgument(IllegalArgumentException ex) {
    ResponseObject body = new ResponseObject();
    body.setSuccess(false);
    body.setMessage(ex.getMessage());
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseObject> handleGeneric(Exception ex) {
    ResponseObject body = new ResponseObject();
    body.setSuccess(false);
    body.setMessage("Internal server error");
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
