package org.example.service;

import org.example.exception.CustomException;
import org.example.model.User;

public interface UserService {
    public enum RoleEnum {
        CLIENT,
        EMPLOYEE
    }

    void save(User user, int code) throws CustomException;
    User findByToken(String token) throws CustomException;
    User findByLogin(String login) throws CustomException;
    User refreshToken(String token, String refreshToken) throws CustomException;
}
