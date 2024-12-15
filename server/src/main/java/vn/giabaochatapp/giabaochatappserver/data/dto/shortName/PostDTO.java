package vn.giabaochatapp.giabaochatappserver.data.dto.shortName;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.Auditable;
import vn.giabaochatapp.giabaochatappserver.data.domains.Post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class PostDTO extends Auditable implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("authorId")
    private Long authorId;
    @JsonProperty("authorName")
    private String authorName;
    @JsonProperty("isBanned")
    private boolean isBanned;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostDTO(PostDTO post) {
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthorId();
        this.isBanned = post.isBanned();
        this.createdAt = post.createdAt;
        this.updatedAt = post.updatedAt;
    }

    public PostDTO(Post post, String authorName) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthorId();
        this.isBanned = post.isBanned();
        this.authorName = authorName;
        this.createdAt = post.createdAt;
        this.updatedAt = post.updatedAt;
    }

}
