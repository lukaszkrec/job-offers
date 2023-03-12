package org.joboffer.domain.loginandregister;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class User {
    private String id;
    private String username;
    private String password;
}
