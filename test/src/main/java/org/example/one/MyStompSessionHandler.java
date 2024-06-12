package org.example.one;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.example.one.utils.MessageSender;
import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/queue/response", this);
        logger.info("Subscribed to /topic/messages");
        session.send("/app/chat.createRoom/MyRoom", null);
        String chatMessage = "hello";
        session.send("/app/chat.sendMessage/664495fa010cd707678b9914", chatMessage);
        logger.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return MessageSender.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        MessageSender messageSender = (MessageSender) payload;
        if (messageSender != null && messageSender.getSenderDto() != null) {
            logger.info("Received : " + messageSender.getSenderDto().getName() + " from : " + messageSender.getSenderDto().getRole());
        } else {
            logger.info("null");
        }
    }

}