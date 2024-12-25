package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.Message;
import vn.giabaochatapp.giabaochatappserver.services.MessageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Message>> getMessagesForRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(messageService.getMessagesForRoom(roomId));
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Map<String, String> messageDetails) {
        Long roomId = Long.valueOf(messageDetails.get("roomId"));
        Long userId = Long.valueOf(messageDetails.get("userId"));
        String username = messageDetails.get("username");
        String content = messageDetails.get("content");
        return ResponseEntity.ok(messageService.saveMessage(roomId, userId, username, content));
    }
}


