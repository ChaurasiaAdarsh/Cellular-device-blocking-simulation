package org.dep.imeiRegistory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle IMEI not found
    @ExceptionHandler(ImeiNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleImeiNotFound(
            ImeiNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("status", 404);
        error.put("error", "IMEI Not Found");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    //handle Invalid Imei Number
    @ExceptionHandler(InvalidImeiException.class)
    public ResponseEntity<Map<String, Object>> invalidImei(
            InvalidImeiException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("status", 404);
        error.put("error", "IMEI Not Found");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                       fieldErrors.put(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        error.put("status", 400);
        error.put("error", "Validation Failed");
        error.put("fieldErrors", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex) {

        Map<String, Object> error = new HashMap<>();
        System.out.println(ex.getClass().getName());

        error.put("status", 500);
        error.put("error", ex.getMessage());
        error.put("message", "Something went wrong");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }


    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleAllHandlerMethodValidationException(
            HandlerMethodValidationException ex) {

        Map<String, Object> error = new HashMap<>();
        System.out.println(ex.getClass().getName());
        String message = ex.getAllErrors() .stream() .map(err -> err.getDefaultMessage()) .filter(msg -> msg != null) .findFirst() .orElse("Validation failed");
        error.put("status", 500);
        error.put("error", message);
        error.put("message", "Invalid Imei number");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}