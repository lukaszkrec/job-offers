package org.joboffer.domain.offer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PrimarySequenceRepository extends MongoRepository<PrimarySequence, String> {
}
