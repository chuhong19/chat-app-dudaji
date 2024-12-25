package vn.giabaochatapp.giabaochatappserver.data.dto.shortName;

import lombok.Data;
import vn.giabaochatapp.giabaochatappserver.data.domains.User;
import vn.giabaochatapp.giabaochatappserver.data.enums.Gender;

import java.io.Serializable;

@Data
public class UserInfoDTO implements Serializable {

    public UserInfoDTO(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.firstname = user.getFirstname();
            this.lastname = user.getLastname();
            this.gender = user.getGender();
        }
    }

    private Long userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Gender gender;

}
