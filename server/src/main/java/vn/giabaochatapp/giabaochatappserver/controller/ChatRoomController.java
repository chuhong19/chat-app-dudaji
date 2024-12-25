package vn.giabaochatapp.giabaochatappserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.ChatRoom;
import vn.giabaochatapp.giabaochatappserver.data.domains.RequestJoinRoom;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.UserInfoResponse;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.UserDTO;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.UserInfoDTO;
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

    @PostMapping("/{roomId}/remove/{userId}")
    public ResponseEntity<Void> removeUserFromRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.removeUserFromRoom(userId, roomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/request/{userId}")
    public ResponseEntity<Void> requestJoinRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.requestJoinRoom(userId, roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}/getAllRequestJoin")
    public List<UserInfoDTO> getAllRequest(@PathVariable Long roomId) {
        return chatRoomService.getAllRequest(roomId);
    }

    @GetMapping("/{roomId}/getAllUser")
    public List<UserInfoDTO> getAllUser(@PathVariable Long roomId) {
        return chatRoomService.getAllUser(roomId);
    }

    @PostMapping("/{roomId}/acceptRequest/{userId}")
    public ResponseEntity<Void> acceptRequestJoinRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.acceptRequestJoinRoom(userId, roomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/declineRequest/{userId}")
    public ResponseEntity<Void> declineRequestJoinRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.declineRequestJoinRoom(userId, roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}/isAdmin/{userId}")
    public Boolean isAdminRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        return chatRoomService.isAdminRoom(roomId, userId);
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<List<ChatRoom>> getPendingRoomsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.getPendingRoomsForUser(userId));
    }

    @GetMapping("/{roomId}/leave/{userId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        chatRoomService.leaveRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

}
