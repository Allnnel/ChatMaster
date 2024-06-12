package org.example.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {
    @JsonProperty("status")
    private String status;

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("jwtToken")
    private String jwtToken;

    public ResponseMessage(String status, String message, int code, String jwtToken) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.jwtToken = jwtToken;
    }
}