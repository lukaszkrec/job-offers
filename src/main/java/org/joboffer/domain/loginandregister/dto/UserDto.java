package org.joboffer.domain.loginandregister.dto;

import lombok.*;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String password;
}
