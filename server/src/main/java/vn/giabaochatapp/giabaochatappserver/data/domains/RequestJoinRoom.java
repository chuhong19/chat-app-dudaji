package vn.giabaochatapp.giabaochatappserver.data.domains;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "request_join_room")
public class RequestJoinRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "room_id", nullable = false)
    public Long chatRoomId;
}
