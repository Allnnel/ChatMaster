package org.example.controller;

import org.example.exception.CustomException;
import org.example.model.Message;
import org.example.model.Room;
import org.example.model.dto.SenderDto;
import org.example.model.utils.ErrorSender;
import org.example.model.utils.MessageSender;
import org.example.service.MessageService;
import org.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

import static java.lang.System.out;


@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RoomService roomService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/a")
    @ResponseBody
    public String a() {
        System.out.println("http to server");
        return "HTTP to server";
    }

    @MessageMapping("/chat.createRoom/{roomName}")
//    @SendTo("/topic/queue/response")
    public void createRoom(@DestinationVariable String roomName, Principal principal) throws CustomException {
        out.println("roomName == " + roomName + ", principal == " + principal);
        if (principal instanceof Authentication authentication) {
            if (authentication.getPrincipal() instanceof SenderDto senderDto) {
                String senderName = senderDto.getName();
                Room room = new Room(roomName, senderName);
                Room roomSaved = roomService.saveRoom(room);
                messagingTemplate.convertAndSend("/topic/queue/response",
                        new MessageSender(roomSaved, new SenderDto(senderDto.getName(), senderDto.getRole())));
            }
        }
        messagingTemplate.convertAndSend("/topic/queue/response", new ErrorSender("TOKEN_NOT_FOUND", 404));
    }

    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(@Payload String message, @DestinationVariable String roomId, Principal principal) {
        out.println("message == " + message + ", roomId == " + roomId + ", principal == " + principal);
        if (principal instanceof Authentication authentication) {
            if (authentication.getPrincipal() instanceof SenderDto senderDto) {
                try {
                    String userName = senderDto.getName();
                    String userRole = senderDto.getRole();
                    Room room = roomService.findById(roomId);
                    Message messageUser = new Message(message, new SenderDto(userName, userRole), room);
                    Message messageSaved = messageService.save(messageUser);
                    messagingTemplate.convertAndSend("/topic/queue/response",
                            new MessageSender(messageSaved, new SenderDto(senderDto.getName(), senderDto.getRole())));
                } catch (CustomException e) {
                    messagingTemplate.convertAndSend("/topic/queue/response", new ErrorSender(e.getMessage(), e.getErrorCode()));
                }
            }
        }
        messagingTemplate.convertAndSend("/topic/queue/response", new ErrorSender("TOKEN_NOT_FOUND", 404));
    }

}