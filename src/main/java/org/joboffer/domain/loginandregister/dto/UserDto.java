package org.joboffer.domain.loginandregister.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
}
