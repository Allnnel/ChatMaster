package org.example.service.impl;


import org.example.exception.CustomException;
import org.example.model.Message;
import org.example.repository.MessageRepository;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message save(Message message) {

        return messageRepository.save(message);
    }

    @Override
    public Message findById(String id) throws CustomException {
        Optional<Message> message = messageRepository.findById(id);
        if(!message.isPresent()) {
            throw new CustomException("MESSAGE_NOT_FOUND", 404);
        }
        return message.get();
    }

    @Override
    public Page<Message> getMessagesByRoomId(String roomId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return messageRepository.findByRoomId(roomId, pageRequest);
    }

    @Override
    public void deleteById(String id) throws CustomException {
        findById(id);
        messageRepository.deleteById(id);
    }

}
