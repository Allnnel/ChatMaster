package org.example.one;

import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class StompClient {

    public static void main(String[] args) {
        String token = "token=eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiRU1QTE9ZRUUiLCJzdWIiOiJyaG9nb3JvbjMiLCJpYXQiOjE3MTU3NjkyMDksImV4cCI6MTcxNTg1NTYwOX0.X2sMT2awB6Xg4yXTSIZTz50TpizfhOHfKzBlo5S055I";
        String wsUrl = "ws://localhost:8087/ws?" + token;

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(wsUrl, sessionHandler);

        new Scanner(System.in).nextLine();
    }
}