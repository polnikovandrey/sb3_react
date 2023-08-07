package com.mcfly.template.payload.user_role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthDataResponse {
    private final Long id;
    private final String email;
    private final String name;
    private final String token;
    private final boolean admin;
}
