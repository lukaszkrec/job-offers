
package org.joboffer.domain.offer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document
class Offer {
    @MongoId
    private String id;
    private String title;
    private String company;
    private String salary;
    @Indexed(unique = true)
    private String offerUrl;
}
