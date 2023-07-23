package com.mcfly.poll.payload.user_role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EditUserFormData {

    @NotNull
    private Long userId;

    @NotBlank
    @Size(min = 4, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20)
    private String lastName;

    @NotBlank
    @Size(max = 20)
    private String middleName;

    @NotNull
    private Integer pageIndex;
}
