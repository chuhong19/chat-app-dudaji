package vn.giabaochatapp.giabaochatappserver.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class GameHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection game established");
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(payload));
        }
    }
}
