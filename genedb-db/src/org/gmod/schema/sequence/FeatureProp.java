package org.gmod.schema.sequence;


import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.utils.Rankable;
import org.gmod.schema.utils.propinterface.PropertyI;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="featureprop")
public class FeatureProp implements Serializable, PropertyI, Rankable {

    // Fields
    @SuppressWarnings("unused")
    @GenericGenerator(name="generator", strategy="seqhilo", parameters = {  @Parameter(name="max_lo", value="100"), @Parameter(name="sequence", value="featureprop_featureprop_id_seq") } )
    @Id
    @GeneratedValue(generator="generator")
    @Column(name="featureprop_id", unique=false, nullable=false, insertable=true, updatable=true)
    private int featurePropId;

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="type_id", unique=false, nullable=false, insertable=true, updatable=true)
    private CvTerm cvTerm;

    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="feature_id", unique=false, nullable=false, insertable=true, updatable=true)
    private Feature feature;

    @Column(name="value", unique=false, nullable=true, insertable=true, updatable=true)
    private String value;

    @Column(name="rank", unique=false, nullable=false, insertable=true, updatable=true)
    private int rank;

    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="featureProp")
    private Collection<FeaturePropPub> featurePropPubs;

     // Constructors
    /** default constructor */
    private FeatureProp() {
        // Deliberately empty default constructor
    }

    /** useful constructor ! */
    public FeatureProp(Feature feature, CvTerm cvTerm, String value, int rank) {
       this.cvTerm = cvTerm;
       this.feature = feature;
       this.value = value;
       this.rank = rank;
    }


    // Property accessors

    public CvTerm getCvTerm() {
        return this.cvTerm;
    }

    private void setCvTerm(CvTerm cvTerm) {
        this.cvTerm = cvTerm;
    }

    private Feature getFeature() {
        return this.feature;
    }

    void setFeature(Feature feature) {
        this.feature = feature;
    }

    public String getValue() {
        return this.value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public int getRank() {
        return this.rank;
    }

    private void setRank(int rank) {
        this.rank = rank;
    }


    private Collection<FeaturePropPub> getFeaturePropPubs() {
        return this.featurePropPubs;
    }

    private void setFeaturePropPubs(Collection<FeaturePropPub> featurePropPubs) {
        this.featurePropPubs = featurePropPubs;
    }

    private int getFeaturePropId() {
        return this.featurePropId;
    }

    private void setFeaturePropId(final int featurePropId) {
        this.featurePropId = featurePropId;
    }

}


