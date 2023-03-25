
package org.joboffer.domain.offer;

import lombok.*;

@Data
@Builder
class Offer {
    private String id;
    private String title;
    private String company;
    private String salary;
    private String offerUrl;
}
