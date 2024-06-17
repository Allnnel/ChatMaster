package org.example.model.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorSender {
    private String error;
    private Integer code;

    public ErrorSender() {
    }

    public ErrorSender(String error, Integer code) {
        this.error = error;
        this.code = code;
    }
}
