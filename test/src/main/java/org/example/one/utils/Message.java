package org.example.one.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
public class Message {

    private String id;
    private String content;
    private SenderDto sender;
    private Room room;

    public Message() {
    }

    public Message(String content, SenderDto sender, Room room) {
        this.content = content;
        this.sender = sender;
        this.room = room;
    }
}
