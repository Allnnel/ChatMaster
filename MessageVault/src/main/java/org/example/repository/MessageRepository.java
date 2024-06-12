package org.example.repository;

import org.example.model.Message;
import org.example.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    @Override
    Optional<Message> save(Message entity);
    Page<Message> findByRoomId(String roomId, Pageable pageable);
    void deleteByRoomId(String id);

}
