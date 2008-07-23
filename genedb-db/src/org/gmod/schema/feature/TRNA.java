package org.gmod.schema.feature;

import java.sql.Timestamp;

import org.gmod.schema.cfg.FeatureType;
import org.gmod.schema.mapped.Organism;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@FeatureType(cv = "sequence", term = "tRNA")
@Indexed
public class TRNA extends Transcript {

    TRNA() {
        // empty
    }

    public TRNA(Organism organism, String systematicId, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, systematicId, analysis, obsolete, dateAccessioned);
    }

    @Override
    @Transient
    public Integer getColourId() {
        return null;
    }

}
