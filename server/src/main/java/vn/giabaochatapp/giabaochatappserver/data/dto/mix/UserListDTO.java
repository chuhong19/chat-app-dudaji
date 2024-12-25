package vn.giabaochatapp.giabaochatappserver.data.dto.mix;

import lombok.Data;
import vn.giabaochatapp.giabaochatappserver.data.dto.shortName.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class UserListDTO implements Serializable {
    private ArrayList<UserDTO> userList;

    public UserListDTO() {
        userList = new ArrayList<>();
    }
}
