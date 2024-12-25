package vn.giabaochatapp.giabaochatappserver.data.domains;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_rooms")
public class ChatRoom extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "[room_name]")
    private String roomName;

    @Column(name = "description")
    private String description;

    @Column(name = "room_creator_id")
    private Long roomCreatorId;

}
