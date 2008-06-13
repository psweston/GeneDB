package org.gmod.schema.organism;

import static javax.persistence.GenerationType.SEQUENCE;

import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.utils.propinterface.PropertyI;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "organismprop")
public class OrganismProp implements Serializable, PropertyI {

    // Fields
    @SequenceGenerator(name = "generator", sequenceName = "organismprop_organismprop_id_seq")
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "organismprop_id", unique = false, nullable = false, insertable = true, updatable = true)
    private int organismPropId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "organism_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Organism organism;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", unique = false, nullable = false, insertable = true, updatable = true)
    private CvTerm cvTerm;

    @Column(name = "value", unique = false, nullable = true, insertable = true, updatable = true)
    private String value;

    @Column(name = "rank", unique = false, nullable = false, insertable = true, updatable = true)
    private int rank;

    // Property accessors

    private int getOrganismPropId() {
        return this.organismPropId;
    }

    private void setOrganismPropId(int organismPropId) {
        this.organismPropId = organismPropId;
    }

    private Organism getOrganism() {
        return this.organism;
    }

    private void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public CvTerm getCvTerm() {
        return this.cvTerm;
    }

    private void setCvTerm(CvTerm cvTerm) {
        this.cvTerm = cvTerm;
    }

    public String getValue() {
        return this.value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    private int getRank() {
        return this.rank;
    }

    private void setRank(int rank) {
        this.rank = rank;
    }
}
