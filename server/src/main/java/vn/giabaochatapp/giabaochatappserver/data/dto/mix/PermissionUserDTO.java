package vn.giabaochatapp.giabaochatappserver.data.dto.mix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionUserDTO {
    private Long permissionId;
    private Long userId;
}
