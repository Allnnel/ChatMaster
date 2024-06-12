package org.example.one.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
public class Room {

    private String id;
    private String name;
    private String sender;

    public Room() {
    }

    public Room(String name, String sender) {
        this.name = name;
        this.sender = sender;
    }

}
