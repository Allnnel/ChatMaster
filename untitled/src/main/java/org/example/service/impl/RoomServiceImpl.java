package org.example.service.impl;


import org.example.exception.CustomException;
import org.example.model.Room;
import org.example.repository.MessageRepository;
import org.example.repository.RoomRepository;
import org.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Room saveRoom(Room room) {
         return roomRepository.save(room);
    }

    @Override
    public Room findById(String id) throws CustomException {
        Optional<Room> room = roomRepository.findById(id);
        if(!room.isPresent()) {
            throw new CustomException("ROOM_NOT_FOUND", 404);
        }
        return room.get();
    }
    @Override
    public void deleteById(String id) throws CustomException {
        findById(id);
        messageRepository.deleteByRoomId(id);
        roomRepository.deleteById(id);
    }
}
