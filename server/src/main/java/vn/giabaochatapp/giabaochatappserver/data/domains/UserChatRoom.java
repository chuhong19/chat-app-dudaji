package vn.giabaochatapp.giabaochatappserver.data.domains;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_chat_rooms")
public class UserChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "chat_room_id", nullable = false)
    public Long chatRoomId;

}
