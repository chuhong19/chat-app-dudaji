package vn.giabaochatapp.giabaochatappserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaochatapp.giabaochatappserver.data.domains.ChatRoom;
import vn.giabaochatapp.giabaochatappserver.data.domains.UserChatRoom;
import vn.giabaochatapp.giabaochatappserver.data.repository.ChatRoomRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.UserChatRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserChatRoomRepository userChatRoomRepository;

    public ChatRoom createRoom(String roomName, String description) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.setDescription(description);
        return chatRoomRepository.save(chatRoom);
    }

    public boolean doesRoomExist(Long roomId) {
        return chatRoomRepository.existsById(roomId);
    }

    public List<ChatRoom> getRoomsForUser(Long userId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByUserId(userId);
        List<Long> roomIds = userChatRooms.stream()
                .map(UserChatRoom::getChatRoomId)
                .collect(Collectors.toList());
        return chatRoomRepository.findAllById(roomIds);
    }

    public void addUserToRoom(Long userId, Long roomId) {
        if (!doesRoomExist(roomId)) {
            throw new IllegalArgumentException("Room does not exist!");
        }

        if (userChatRoomRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            throw new IllegalArgumentException("User already in room!");
        }

        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setUserId(userId);
        userChatRoom.setChatRoomId(roomId);
        userChatRoomRepository.save(userChatRoom);
    }

    public void removeUserFromRoom(Long userId, Long roomId) {
        if (!userChatRoomRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            throw new IllegalArgumentException("User is not in the room!");
        }

        userChatRoomRepository.deleteByUserIdAndChatRoomId(userId, roomId);
    }

    public boolean isUserInRoom(Long userId, Long roomId) {
        return userChatRoomRepository.existsByUserIdAndChatRoomId(userId, roomId);
    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

}


