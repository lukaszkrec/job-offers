package org.joboffer.domain.loginandregister;

import lombok.*;

@Data
@Builder
class User {
    private String id;
    private String username;
    private String password;
}
