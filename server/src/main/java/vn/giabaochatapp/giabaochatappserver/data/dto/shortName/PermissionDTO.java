package vn.giabaochatapp.giabaochatappserver.data.dto.shortName;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaochatapp.giabaochatappserver.data.domains.Permission;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class PermissionDTO implements Serializable {

    private Long id;
    private String permission;
    private boolean enabled;
    private String note;

    public PermissionDTO(Permission permission) {
        this.id = permission.getId();
        this.permission = permission.getPermission();
        this.enabled = permission.isEnabled();
        this.note = permission.getNote();
    }

}
