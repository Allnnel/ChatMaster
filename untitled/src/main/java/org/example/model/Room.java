package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "room")
@Getter
@Setter
public class Room {

    @Id
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
