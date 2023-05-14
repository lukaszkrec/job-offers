package org.joboffer.domain.offer;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
class PrimarySequence {
    @Id
    private String id;
    private long seq;
}