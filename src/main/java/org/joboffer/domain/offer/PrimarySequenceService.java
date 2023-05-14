package org.joboffer.domain.offer;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

class PrimarySequenceService {

    private static final String PRIMARY_SEQUENCE = "primarySequence";

    private final MongoOperations mongoOperations;

    PrimarySequenceService(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    long getNextValue() {
        PrimarySequence primarySequence = mongoOperations.findAndModify(
                query(where("_id").is(PRIMARY_SEQUENCE)),
                new Update().inc("seq", 1000),
                options().returnNew(true),
                PrimarySequence.class);
        if (primarySequence == null) {
            primarySequence = new PrimarySequence();
            primarySequence.setId(PRIMARY_SEQUENCE);
            primarySequence.setSeq(1000);
            mongoOperations.insert(primarySequence);
        }
        return primarySequence.getSeq();
    }

}