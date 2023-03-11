package org.joboffer.domain.loginandregister.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
class UserDto {
    private final String username;
    private final String password;
}
