package vn.giabaochatapp.giabaochatappserver.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;
import vn.giabaochatapp.giabaochatappserver.data.domains.Message;
import vn.giabaochatapp.giabaochatappserver.services.ChatRoomService;
import vn.giabaochatapp.giabaochatappserver.services.MessageService;

import java.util.Map;

@Component
public class ChatRoomHandler extends TextWebSocketHandler {

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = getRoomIdFromSession(session);
        roomManager.addSessionToRoom(roomId, session);
        System.out.println("New connection established in room: " + roomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> messageData = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

        Long roomId = Long.valueOf(messageData.get("roomId").toString());
        Long userId = Long.valueOf(messageData.get("userId").toString());
        String username = messageData.get("username").toString();
        String content = messageData.get("content").toString();
        String createdAt = messageData.get("createdAt").toString();

        Message savedMessage = messageService.saveMessageWithClientTime(roomId, userId, username, content, createdAt);

        Map<String, Object> response = Map.of(
                "username", savedMessage.getUsername(),
                "content", savedMessage.getContent(),
                "roomId", savedMessage.getRoomId(),
                "createdAt", savedMessage.getCreatedAt().toString()
        );

        String broadcastMessage = objectMapper.writeValueAsString(response);
        roomManager.broadcastMessageToRoom(roomId.toString(), broadcastMessage, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomId = getRoomIdFromSession(session);
        roomManager.removeSessionFromRoom(roomId, session);
        System.out.println("Connection closed in room: " + roomId);
    }

    private String getRoomIdFromSession(WebSocketSession session) {
        String uri = session.getUri().toString();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }
}






