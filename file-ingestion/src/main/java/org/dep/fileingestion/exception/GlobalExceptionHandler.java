package org.dep.fileingestion.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<Map<String, Object>> invalidFile(
            InvalidFileException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("status", 404);
        error.put("error", "Invalid File Please Check Try Again");
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(
            Exception ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("status", 500);
        error.put("error", "Internal Server Error");
        error.put("message", "Something went wrong");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
