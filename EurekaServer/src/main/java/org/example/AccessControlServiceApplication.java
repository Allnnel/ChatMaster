package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// 11
@SpringBootApplication
@EnableEurekaServer
public class AccessControlServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccessControlServiceApplication.class, args);
    }
}

