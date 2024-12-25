package vn.giabaochatapp.giabaochatappserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.giabaochatapp.giabaochatappserver.data.domains.Message;
import vn.giabaochatapp.giabaochatappserver.data.repository.MessageRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    public Message saveMessage(Long roomId, Long userId, String username, String content) {
        if (!chatRoomService.doesRoomExist(roomId)) {
            throw new IllegalArgumentException("Room does not exist!");
        }

        if (!chatRoomService.isUserInRoom(userId, roomId)) {
            throw new IllegalArgumentException("User is not a member of this room!");
        }

        Message message = new Message();
        message.setRoomId(roomId);
        message.setUserId(userId);
        message.setUsername(username);
        message.setContent(content);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesForRoom(Long roomId) {
        return messageRepository.findByRoomId(roomId);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found with id: " + id));
    }

    public Message saveMessageWithClientTime(Long roomId, Long userId, String username, String content, String createdAt) {
        Message message = new Message();
        message.setRoomId(roomId);
        message.setUserId(userId);
        message.setUsername(username);
        message.setContent(content);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            LocalDateTime clientTime = LocalDateTime.parse(createdAt, formatter);
            message.setCreatedAt(clientTime);
        } catch (Exception e) {
            e.printStackTrace();
            message.setCreatedAt(LocalDateTime.now());
        }

        return messageRepository.save(message);
    }
}


