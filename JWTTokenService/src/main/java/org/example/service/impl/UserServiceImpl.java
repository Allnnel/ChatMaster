package org.example.service.impl;

import org.example.exception.CustomException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final int CODE = 1111;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user, int code) throws CustomException {
        validateCode(code);
        validateLogin(user.getLogin());
        validatePassword(user.getPassword());
        validatePhoneNumber(user.getPhone());
        validateRole(user.getRole());
        Optional<User> userRepositoryByPhone = userRepository.findByPhone(user.getPhone());
        if (userRepositoryByPhone.isPresent()) {
            throw new CustomException("NUMBER_ALREADY_EXISTS", 409);
        }
        Optional<User> userRepositoryByLogin = userRepository.findByLogin(user.getLogin());
        if (userRepositoryByLogin.isPresent()) {
            throw new CustomException("LOGIN_ALREADY_EXISTS", 409);
        }
        userRepository.save(user);
    }

    @Override
    public User refreshToken(String token, String refreshToken) throws CustomException {
        User user = findByToken(token);
        user.setToken(refreshToken);
        userRepository.save(user);
        return findByToken(refreshToken);
    }

    @Override
    public User findByToken(String token) throws CustomException {
        Optional<User> user = userRepository.findByToken(token);
        if (!user.isPresent()) {
            throw new CustomException("TOKEN_NOT_FOUND", 404);
        }
        return user.get();
    }

    @Override
    public User findByLogin(String login) throws CustomException {
        Optional<User> user = userRepository.findByLogin(login);
        if (!user.isPresent()) {
            throw new CustomException("LOGIN_NOT_FOUND", 404);
        }
        return user.get();
    }

    private void validatePhoneNumber(String phoneNumber) throws CustomException {
        String strippedNumber = phoneNumber.replaceAll("[^0-9]", "");
        if (strippedNumber.length() < 10 || strippedNumber.length() > 15) {
            throw new CustomException("PHONE_NUMBER_LENGTH_INVALID", 422);
        }
        if (!strippedNumber.matches("\\d{10,}")) {
            throw new CustomException("PHONE_NUMBER_INVALID", 422);
        }
    }

    private void validateCode(int code) throws CustomException {
        if (code != CODE) {
            throw  new CustomException("CODE_INVALID", 422);
        }
    }

    public void validateRole(String role) throws CustomException {
        try {
            RoleEnum.valueOf(role);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CustomException("ROLE_INVALID", 422);
        }
    }

    private void validateLogin(String login) throws CustomException {
        if (login.length() < 6 || login.length() > 20) {
            throw new CustomException("LOGIN_LENGTH_INVALID", 422);
        }
        if (!login.matches("^[a-zA-Z0-9_-]+$")) {
            throw new CustomException("LOGIN_FORMAT_INVALID", 422);
        }
    }

    private void validatePassword(char[] password) throws CustomException {
//        if (password.length < 8 || password.length > 20) {
//            throw new CustomException("PASSWORD_LENGTH_INVALID", 422);
//        }
    }

}
