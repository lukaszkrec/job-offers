package org.joboffer.domain.loginandregister;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
class User {
    private final Long id;
    private final String username;
    private final String password;
}
