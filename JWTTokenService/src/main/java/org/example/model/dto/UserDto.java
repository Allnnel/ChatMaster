package org.example.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String login;
    private String role;
    private String phone;

    public UserDto() {
    }

    public UserDto(String login, String role, String phone) {
        this.login = login;
        this.role = role;
        this.phone = phone;
    }
}
