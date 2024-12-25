package vn.giabaochatapp.giabaochatappserver.data.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "file_name", nullable = false)
    public String fileName;

    public String url;

    public Long chatRoomId;

    @Column(name = "uploaded_at", nullable = false)
    public LocalDateTime uploadedAt;

}
