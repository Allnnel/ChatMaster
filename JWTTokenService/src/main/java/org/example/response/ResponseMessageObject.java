package org.example.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessageObject extends ResponseMessage{
    @JsonProperty("object")
    private Object object;
    public ResponseMessageObject(String status, String message, int code, String jwtToken, Object object) {
        super(status, message, code, jwtToken);
        this.object = object;
    }
}
