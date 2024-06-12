package org.example.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception {
    private final int errorCode;
    private final String message;
    public CustomException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}