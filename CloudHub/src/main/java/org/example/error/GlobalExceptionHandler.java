package org.example.error;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.attribute.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import static java.lang.System.out;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {HttpClientErrorException.class, HttpServerErrorException.class, ResourceAccessException.class})
    public ResponseEntity<ResponseMessage> handleHttpExceptions(Exception ex)  {
        out.println("111111");
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // По умолчанию внутренняя ошибка сервера

        if (ex instanceof HttpClientErrorException) {
            status = (HttpStatus) ((HttpClientErrorException) ex).getStatusCode();
        } else if (ex instanceof HttpServerErrorException) {
            status = (HttpStatus) ((HttpServerErrorException) ex).getStatusCode();
        } else if (ex instanceof ResourceAccessException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
        }

        // Получаем JSON из сообщения исключения
        String jsonErrorMessage = extractJsonErrorMessage(ex.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ResponseMessage errorResponse = objectMapper.readValue(jsonErrorMessage, ResponseMessage.class);
            return ResponseEntity.status(status).body(errorResponse);
        } catch (JsonProcessingException e) {
            // В случае ошибки парсинга возвращаем стандартное сообщение об ошибке
            return ResponseEntity.status(status).body(new ResponseMessage("fail", e.getMessage(), status.value(), null));
        }
    }

    // Метод для извлечения JSON из сообщения исключения
    private String extractJsonErrorMessage(String errorMessage) {
        // Находим индекс начала JSON-строки
        int startIndex = errorMessage.indexOf('{');
        // Находим индекс конца JSON-строки
        int endIndex = errorMessage.lastIndexOf('}');
        // Вырезаем JSON-строку из сообщения исключения
        return errorMessage.substring(startIndex, endIndex + 1);
    }
}
