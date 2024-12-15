package vn.giabaochatapp.giabaochatappserver.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaochatapp.giabaochatappserver.data.enums.Gender;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest implements Serializable {
    private Long userId;
    private String firstname;
    private String lastname;
    private Gender gender;

}
