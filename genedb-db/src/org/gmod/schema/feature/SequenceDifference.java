package org.gmod.schema.feature;

import org.gmod.schema.cfg.FeatureType;

import javax.persistence.Entity;

/**
 *
 */
@Entity
@FeatureType(cv="sequence", term="sequence_difference")
public class SequenceDifference extends Region {
    //private static final Logger logger = Logger.getLogger(SequenceDifference.class);

}
