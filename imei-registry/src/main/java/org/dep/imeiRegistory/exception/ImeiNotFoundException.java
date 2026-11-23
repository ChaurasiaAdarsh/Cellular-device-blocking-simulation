package org.dep.imeiRegistory.exception;

//a separate exception that can be thrown
public class ImeiNotFoundException extends RuntimeException {

    public ImeiNotFoundException(String message) {
        super(message);
    }
}
