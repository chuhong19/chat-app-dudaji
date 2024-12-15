package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.ChatRoom;
import vn.giabaochatapp.giabaochatappserver.services.ChatRoomService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoom> createRoom(@RequestBody Map<String, String> roomDetails) {
        String name = roomDetails.get("name");
        String description = roomDetails.get("description");
        return ResponseEntity.ok(chatRoomService.createRoom(name, description));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        return ResponseEntity.ok(chatRoomService.getAllRooms());
    }

    @GetMapping("/joined/{userId}")
    public ResponseEntity<List<ChatRoom>> getRoomsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.getRoomsForUser(userId));
    }

    @PostMapping("/{roomId}/join/{userId}")
    public ResponseEntity<Void> addUserToRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.addUserToRoom(userId, roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatRoomService.getRoomById(roomId));
    }

}
