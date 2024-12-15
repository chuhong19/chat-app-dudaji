package vn.giabaochatapp.giabaochatappserver.data.domains;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "posts_comment")
public class PostComment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name = "post_id", nullable = false)
    public Long postId;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "content", nullable = false)
    public String content;

    public PostComment(Long postId, Long userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
