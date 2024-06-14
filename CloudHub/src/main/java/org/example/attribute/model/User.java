package org.example.attribute.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String login;
    private String role;
    private String phone;

    public User() {
    }

    public User(String login, String role, String phone) {
        this.login = login;
        this.role = role;
        this.phone = phone;
    }
}
