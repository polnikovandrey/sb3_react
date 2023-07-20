package com.mcfly.poll.payload.user_role;

import com.mcfly.poll.domain.user_role.RoleName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Builder
@Getter
@Setter
@ToString
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private Set<RoleName> roles;

}
