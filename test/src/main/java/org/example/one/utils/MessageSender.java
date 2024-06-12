package org.example.one.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageSender {
    private Object object;
    private SenderDto senderDto;

    public MessageSender() {}

    public MessageSender(Object object, SenderDto senderDto) {
        this.object = object;
        this.senderDto = senderDto;
    }
}
