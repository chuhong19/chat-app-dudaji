package vn.giabaochatapp.giabaochatappserver.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentRequest {
    private Long postId;
    private Long commentId;
}