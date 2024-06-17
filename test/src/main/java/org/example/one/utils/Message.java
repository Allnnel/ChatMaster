package org.example.one.utils;

import lombok.Getter;
import lombok.Setter;

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
