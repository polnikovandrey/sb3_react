package com.mcfly.poll.payload.user_role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UpdateUserDataRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20)
    private String lastName;

    @NotNull
    @Size(max = 20)
    private String middleName;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 15)
    private String name;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
