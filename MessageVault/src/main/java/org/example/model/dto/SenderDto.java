package org.example.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenderDto {
    private String id;
    private String name;
    private String role;
    public SenderDto() {
    }
    public SenderDto(String name, String role) {
        this.name = name;
        this.role = role;
    }
}

