package org.example.model.utils;

import lombok.Getter;
import lombok.Setter;
import org.example.model.dto.SenderDto;

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
