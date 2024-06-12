package org.example.model;

import lombok.Getter;
import lombok.Setter;
import org.example.exception.CustomException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Getter
@Setter
@Table(name = "user_chat")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private char[] password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "phone", nullable = false)
    private String phone;

    public User() {
    }

    public User(String login, char[] password, String role, String token, String phone) {
        this.login = login;
        setPassword(password);
        this.role = role;
        this.token = token;
        this.phone = phone;
    }

    public void setPassword(char[] password) {
        this.password = PasswordUtils.encryptPassword(password);
        Arrays.fill(password, '\0');
    }

    public static class PasswordUtils {
        private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Метод для шифрования пароля
        public static char[] encryptPassword(char[] password) {
            return passwordEncoder.encode(new String(password)).toCharArray();
        }

        // Метод для сравнения зашифрованного пароля с введенным пользователем
        public static void matches(char[] rawPassword, char[] encodedPassword) throws CustomException {
            String rawPasswordString = new String(rawPassword);
            String encodedPasswordString = new String(encodedPassword);
            if (!passwordEncoder.matches(rawPasswordString, encodedPasswordString)) {
                throw new CustomException("INVALID_PASSWORD", 401);
            }
        }

    }

}