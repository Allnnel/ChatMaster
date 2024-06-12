package org.example;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Arrays;


@SpringBootApplication
public class AccessControlServiceApplication implements CommandLineRunner {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(AccessControlServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        String token = jwtTokenProvider.generateToken("admin_1", "ADMIN");
        char [] password = {'s', '9', 'K', 'H', '8', '1', 'h', 'O', 'T'};
        userRepository.save(new User("admin_1", password, "ADMIN", token, "1234567890"));
        Arrays.fill(password, '\0');
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}