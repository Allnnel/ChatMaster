package org.example.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.attribute.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(value = {HttpClientErrorException.class, HttpServerErrorException.class, ResourceAccessException.class, RuntimeException.class})
    public ResponseEntity<ResponseMessage> handleHttpExceptions(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String jsonErrorMessage = extractJsonErrorMessage(ex.getMessage());
        logger.info("jsonErrorMessage : {}", ex.getMessage());
        try {
            ResponseMessage errorResponse = objectMapper.readValue(jsonErrorMessage, ResponseMessage.class);
            return ResponseEntity.status(status).body(errorResponse);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(status).body(new ResponseMessage("Failed", status.value(), e.getMessage(), null));
        }
    }

    private String extractJsonErrorMessage(String errorMessage) {
        int startIndex = errorMessage.indexOf('{');
        int endIndex = errorMessage.lastIndexOf('}');
        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            return errorMessage;
        }
        return errorMessage.substring(startIndex, endIndex + 1);
    }
}
