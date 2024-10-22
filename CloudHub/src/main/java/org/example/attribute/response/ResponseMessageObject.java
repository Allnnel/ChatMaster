package org.example.attribute.response;

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
    public ResponseMessageObject() {
    }
    public ResponseMessageObject(String status, String message, Integer code, String jwtToken, Object object) {
        super(status, code, message, jwtToken);
        this.object = object;
    }
}
