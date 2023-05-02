
package org.joboffer.domain.offer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.annotation.Generated;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Document
class Offer {
    @Id
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String company;
    @NonNull
    private String salary;
    @Indexed(unique = true)
    @NonNull
    private String offerUrl;
}
