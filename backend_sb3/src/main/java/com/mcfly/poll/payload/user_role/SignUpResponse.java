package com.mcfly.poll.payload.user_role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignUpResponse {

    private Long id;
    private String email;
    private String name;
    private String token;
    private boolean admin;

}
