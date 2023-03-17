
package org.joboffer.domain.offer;

import lombok.*;

@Data
@Builder
class Offer {
    private String id;
    private String url;
    private String workSite;
    private String company;
    private String salary;
}
