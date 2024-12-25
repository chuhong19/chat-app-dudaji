package vn.giabaochatapp.giabaochatappserver.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.giabaochatapp.giabaochatappserver.config.exception.NotFoundException;
import vn.giabaochatapp.giabaochatappserver.config.exception.PermissionNotFoundException;
import vn.giabaochatapp.giabaochatappserver.data.domains.ChatRoom;
import vn.giabaochatapp.giabaochatappserver.data.domains.RequestJoinRoom;
import vn.giabaochatapp.giabaochatappserver.data.domains.User;
import vn.giabaochatapp.giabaochatappserver.data.domains.UserChatRoom;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.UserInfoDTO;
import vn.giabaochatapp.giabaochatappserver.data.repository.ChatRoomRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.RequestJoinRoomRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.UserChatRoomRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserChatRoomRepository userChatRoomRepository;

    @Autowired
    private RequestJoinRoomRepository requestJoinRoomRepository;

    public ChatRoom createRoom(String roomName, String description) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long userId = principal.getId();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.setDescription(description);
        chatRoom.setRoomCreatorId(userId);
        chatRoomRepository.save(chatRoom);

        addUserToRoom(userId, chatRoom.getId());
        return chatRoom;
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

    @Transactional
    public void removeUserFromRoom(Long userId, Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long hostId = principal.getId();
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        if (!Objects.equals(hostId, chatRoom.getRoomCreatorId())) {
            throw new PermissionNotFoundException("Only host can remove user");
        }
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

    public void requestJoinRoom(Long userId, Long roomId) {
        if (!doesRoomExist(roomId)) {
            throw new IllegalArgumentException("Room does not exist!");
        }

        if (userChatRoomRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            throw new IllegalArgumentException("User already in room!");
        }

        if (requestJoinRoomRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            throw new IllegalArgumentException("Duplicate request already exists!");
        }

        RequestJoinRoom requestJoinRoom = new RequestJoinRoom();
        requestJoinRoom.setUserId(userId);
        requestJoinRoom.setChatRoomId(roomId);
        requestJoinRoomRepository.save(requestJoinRoom);
    }


    public void acceptRequestJoinRoom(Long userRequestId, Long roomId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long hostId = principal.getId();

        if (!Objects.equals(hostId, chatRoom.getRoomCreatorId())) {
            throw new PermissionNotFoundException("Only host can accept request");
        }
        Optional<RequestJoinRoom> requestJoinRoomOpt = requestJoinRoomRepository.findByUserIdAndChatRoomId(userRequestId, roomId);
        if (requestJoinRoomOpt.isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        RequestJoinRoom requestJoinRoom = requestJoinRoomOpt.get();
        addUserToRoom(userRequestId, roomId);
        requestJoinRoomRepository.delete(requestJoinRoom);
    }

    public void declineRequestJoinRoom(Long userRequestId, Long roomId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Long hostId = principal.getId();

        if (!Objects.equals(hostId, chatRoom.getRoomCreatorId())) {
            throw new PermissionNotFoundException("Only host can decline request");
        }
        Optional<RequestJoinRoom> requestJoinRoomOpt = requestJoinRoomRepository.findByUserIdAndChatRoomId(userRequestId, roomId);
        if (requestJoinRoomOpt.isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        RequestJoinRoom requestJoinRoom = requestJoinRoomOpt.get();
        requestJoinRoomRepository.delete(requestJoinRoom);
    }

    public boolean isAdminRoom (Long roomId, Long userId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new NotFoundException("Room not found");
        }
        ChatRoom chatRoom = chatRoomOpt.get();
        return Objects.equals(userId, chatRoom.getRoomCreatorId());
    }

    public List<UserInfoDTO> getAllRequest(Long roomId) {
        return requestJoinRoomRepository.findAll().stream()
                .filter(request -> request.getChatRoomId().equals(roomId))
                .map(request -> {
                    Optional<User> userOpt = userRepository.findById(request.getUserId());
                    if (userOpt.isEmpty()) {
                        throw new NotFoundException("User not exist");
                    }
                    User user = userOpt.get();
                    return new UserInfoDTO(user);
                })
                .collect(Collectors.toList());
    }

    public List<UserInfoDTO> getAllUser(Long roomId) {
        return userChatRoomRepository.findAll().stream()
                .filter(record -> record.getChatRoomId().equals(roomId))
                .map(record -> {
                    Optional<User> userOpt = userRepository.findById(record.getUserId());
                    if (userOpt.isEmpty()) {
                        throw new NotFoundException("User not exist");
                    }
                    User user = userOpt.get();
                    return new UserInfoDTO(user);
                })
                .collect(Collectors.toList());
    }

    public List<ChatRoom> getPendingRoomsForUser(Long userId) {
        List<RequestJoinRoom> pendingRequests = requestJoinRoomRepository.findByUserId(userId);

        List<Long> roomIds = pendingRequests.stream()
                .map(RequestJoinRoom::getChatRoomId)
                .collect(Collectors.toList());
        return chatRoomRepository.findAllById(roomIds);
    }

    @Transactional
    public void leaveRoom(Long roomId, Long userId) {
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findById(roomId);
        if (chatRoomOpt.isEmpty()) {
            throw new NotFoundException("Room not found");
        }

        ChatRoom chatRoom = chatRoomOpt.get();

        if (Objects.equals(chatRoom.getRoomCreatorId(), userId)) {
            throw new IllegalArgumentException("Admin cannot leave the room");
        }

        if (!userChatRoomRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            throw new IllegalArgumentException("User is not in the room");
        }

        userChatRoomRepository.deleteByUserIdAndChatRoomId(userId, roomId);
    }
}