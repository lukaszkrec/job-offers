package org.joboffer.domain.loginandregister;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document
class User {
    @MongoId
    private String id;
    private String username;
    private String password;
}
