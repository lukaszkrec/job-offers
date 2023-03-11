package org.joboffer.domain.loginandregister;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
class User {
    private final String id;
    private final String username;
    private final String password;
}
