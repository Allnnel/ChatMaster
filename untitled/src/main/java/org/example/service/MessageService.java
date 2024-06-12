package org.example.service;

import org.example.exception.CustomException;
import org.example.model.Message;
import org.springframework.data.domain.Page;

public interface MessageService {
    Message save(Message message);
    Page<Message> getMessagesByRoomId(String roomId, int page, int size);
    Message findById(String id) throws CustomException;
    void deleteById(String id) throws CustomException;
}
