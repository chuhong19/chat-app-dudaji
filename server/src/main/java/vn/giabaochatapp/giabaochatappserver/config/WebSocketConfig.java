package vn.giabaochatapp.giabaochatappserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatRoomHandler chatRoomHandler;

    public WebSocketConfig(ChatRoomHandler chatRoomHandler) {
        this.chatRoomHandler = chatRoomHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatHandler(), "/community")
                .setAllowedOrigins("*");
        registry.addHandler(new GameHandler(), "/tictactoe")
                .setAllowedOrigins("*");
        registry.addHandler(new NotificationHandler(), "/notifications")
                .setAllowedOrigins("*");
        registry.addHandler(chatRoomHandler, "/chatroom/{roomId}")
                .setAllowedOrigins("*");

    }
}
