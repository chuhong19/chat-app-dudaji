package vn.giabaochatapp.giabaochatappserver.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomManager {

    private final Map<String, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    public void addSessionToRoom(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> new ArrayList<>()).add(session);
    }

    public void removeSessionFromRoom(String roomId, WebSocketSession session) {
        List<WebSocketSession> sessions = rooms.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                rooms.remove(roomId);
            }
        }
    }

    public void broadcastMessageToRoom(String roomId, String message, WebSocketSession sender) {
        List<WebSocketSession> sessions = rooms.getOrDefault(roomId, new ArrayList<>());
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


