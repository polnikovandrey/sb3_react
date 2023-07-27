package com.mcfly.poll.payload.user_role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddUserRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20)
    private String lastName;

    @NotBlank
    @Size(max = 20)
    private String middleName;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    private boolean admin;
}
