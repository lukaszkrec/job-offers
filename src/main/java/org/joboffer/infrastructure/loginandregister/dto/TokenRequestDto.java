package org.joboffer.infrastructure.loginandregister.dto;

import javax.validation.constraints.NotBlank;

public record TokenRequestDto(
        @NotBlank(message = "Username can not be empty")
        String username,

        @NotBlank(message = "Password can not be empty")
        String password
) {
}
