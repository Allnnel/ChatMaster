package org.example.attribute.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

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

    public ResponseMessage() {
    }

    public ResponseMessage(String status, String message, int code, String jwtToken) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.jwtToken = jwtToken;
    }

    // Фабричный метод для создания объекта ResponseMessage из JSON
    public static ResponseMessage createFromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ResponseMessage.class);
    }
}
