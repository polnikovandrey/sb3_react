package com.mcfly.poll.payload.user_role;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UserDataResponse {
    private Long id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String middleName;
    private boolean admin;
}
