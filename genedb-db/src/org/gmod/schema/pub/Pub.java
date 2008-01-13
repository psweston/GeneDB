package org.gmod.schema.pub;


import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.phylogeny.PhylonodePub;
import org.gmod.schema.phylogeny.PhylotreePub;
import org.gmod.schema.sequence.FeatureCvTerm;
import org.gmod.schema.sequence.FeatureCvTermPub;
import org.gmod.schema.sequence.FeatureLocPub;
import org.gmod.schema.sequence.FeaturePropPub;
import org.gmod.schema.sequence.FeaturePub;
import org.gmod.schema.sequence.FeatureRelationshipPropPub;
import org.gmod.schema.sequence.FeatureRelationshipPub;
import org.gmod.schema.sequence.FeatureSynonym;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="pub")
public class Pub implements Serializable {
    
    
    private Set<PhylotreePub> phylotreePubs = new HashSet<PhylotreePub>(0);
    private Set<PhylonodePub> phylonodePubs = new HashSet<PhylonodePub>(0);
    
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="pub")
    public Set<PhylotreePub> getPhylotreePubs() {
        return this.phylotreePubs;
    }
    
    public void setPhylotreePubs(Set<PhylotreePub> phylotreePubs) {
        this.phylotreePubs = phylotreePubs;
    }
    
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="pub")
    public Set<PhylonodePub> getPhylonodePubs() {
        return this.phylonodePubs;
    }
    
    public void setPhylonodePubs(Set<PhylonodePub> phylonodePubs) {
        this.phylonodePubs = phylonodePubs;
    }

    // Fields    
     @Id
    
    @Column(name="pub_id", unique=false, nullable=false, insertable=true, updatable=true)
     private int pubId;
     
     @ManyToOne(cascade={}, fetch=FetchType.LAZY)
         
         @JoinColumn(name="type_id", unique=false, nullable=false, insertable=true, updatable=true)
     private CvTerm cvTerm;
     
     @Column(name="title", unique=false, nullable=true, insertable=true, updatable=true)
     private String title;
     
     @Column(name="volumetitle", unique=false, nullable=true, insertable=true, updatable=true)
     private String volumeTitle;
     
     @Column(name="volume", unique=false, nullable=true, insertable=true, updatable=true)
     private String volume;
     
     @Column(name="series_name", unique=false, nullable=true, insertable=true, updatable=true)
     private String seriesName;
     
     @Column(name="issue", unique=false, nullable=true, insertable=true, updatable=true)
     private String issue;
     
     @Column(name="pyear", unique=false, nullable=true, insertable=true, updatable=true)
     private String pyear;
     
     @Column(name="pages", unique=false, nullable=true, insertable=true, updatable=true)
     private String pages;
     
     @Column(name="miniref", unique=false, nullable=true, insertable=true, updatable=true)
     private String miniRef;
     
     @Column(name="uniquename", unique=true, nullable=false, insertable=true, updatable=true)
     private String uniqueName;
     
     @Column(name="is_obsolete", unique=false, nullable=true, insertable=true, updatable=true)
     private Boolean obsolete;
     
     @Column(name="publisher", unique=false, nullable=true, insertable=true, updatable=true)
     private String publisher;
     
     @Column(name="pubplace", unique=false, nullable=true, insertable=true, updatable=true)
     private String pubPlace;
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<PubAuthor> pubAuthors = new HashSet<PubAuthor>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pubByObjectId")
     private Set<PubRelationship> pubRelationshipsForObjectId = new HashSet<PubRelationship>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<PubDbXRef> pubDbXRefs = new HashSet<PubDbXRef>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeatureCvTerm> featureCvTerms = new HashSet<FeatureCvTerm>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeatureRelationshipPub> featureRelationshipPubs = new HashSet<FeatureRelationshipPub>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeaturePub> featurePubs = new HashSet<FeaturePub>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeaturePropPub> featurePropPubs = new HashSet<FeaturePropPub>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeatureSynonym> featureSynonyms = new HashSet<FeatureSynonym>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeatureCvTermPub> featureCvTermPubs = new HashSet<FeatureCvTermPub>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeatureRelationshipPropPub> featureRelationshipPropPubs = new HashSet<FeatureRelationshipPropPub>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<PubProp> pubProps = new HashSet<PubProp>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pubBySubjectId")
     private Set<PubRelationship> pubRelationshipsForSubjectId = new HashSet<PubRelationship>(0);
     
     @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="pub")
     private Set<FeatureLocPub> featureLocPubs = new HashSet<FeatureLocPub>(0);

     // Constructors

    /** default constructor */
    public Pub() {
    	// Deliberately empty default constructor
    }

	/** minimal constructor */
    public Pub(String uniqueName, CvTerm cvTerm) {
        this.uniqueName = uniqueName;
        this.cvTerm = cvTerm;
    }
    
    public Pub(String uniqueName) {
        this.uniqueName = uniqueName;
    }
    
   
    // Property accessors

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubId()
     */
    public int getPubId() {
        return this.pubId;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubId(int)
     */
    public void setPubId(int pubId) {
        this.pubId = pubId;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getCvTerm()
     */
    public CvTerm getCvTerm() {
        return this.cvTerm;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setCvTerm(org.gmod.schema.cv.CvTermI)
     */
    public void setCvTerm(CvTerm cvTerm) {
        this.cvTerm = cvTerm;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getTitle()
     */
    public String getTitle() {
        return this.title;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        this.title = title;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getVolumeTitle()
     */
    public String getVolumeTitle() {
        return this.volumeTitle;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setVolumeTitle(java.lang.String)
     */
    public void setVolumeTitle(String volumeTitle) {
        this.volumeTitle = volumeTitle;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getVolume()
     */
    public String getVolume() {
        return this.volume;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setVolume(java.lang.String)
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getSeriesName()
     */
    public String getSeriesName() {
        return this.seriesName;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setSeriesName(java.lang.String)
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getIssue()
     */
    public String getIssue() {
        return this.issue;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setIssue(java.lang.String)
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPyear()
     */
    public String getPyear() {
        return this.pyear;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPyear(java.lang.String)
     */
    public void setPyear(String pyear) {
        this.pyear = pyear;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPages()
     */
    public String getPages() {
        return this.pages;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPages(java.lang.String)
     */
    public void setPages(String pages) {
        this.pages = pages;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getMiniRef()
     */
    public String getMiniRef() {
        return this.miniRef;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setMiniRef(java.lang.String)
     */
    public void setMiniRef(String miniRef) {
        this.miniRef = miniRef;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getUniqueName()
     */
    public String getUniqueName() {
        return this.uniqueName;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setUniqueName(java.lang.String)
     */
    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getobsolete()
     */
    public Boolean getObsolete() {
        return this.obsolete;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setobsolete(java.lang.Boolean)
     */
    public void setObsolete(Boolean obsolete) {
        this.obsolete = obsolete;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPublisher()
     */
    public String getPublisher() {
        return this.publisher;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPublisher(java.lang.String)
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubPlace()
     */
    public String getPubPlace() {
        return this.pubPlace;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubPlace(java.lang.String)
     */
    public void setPubPlace(String pubPlace) {
        this.pubPlace = pubPlace;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubAuthors()
     */
    public Collection<PubAuthor> getPubAuthors() {
        return this.pubAuthors;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubAuthors(java.util.Set)
     */
    public void setPubAuthors(Set<PubAuthor> pubAuthors) {
        this.pubAuthors = pubAuthors;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubRelationshipsForObjectId()
     */
    public Collection<PubRelationship> getPubRelationshipsForObjectId() {
        return this.pubRelationshipsForObjectId;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubRelationshipsForObjectId(java.util.Set)
     */
    public void setPubRelationshipsForObjectId(Set<PubRelationship> pubRelationshipsForObjectId) {
        this.pubRelationshipsForObjectId = pubRelationshipsForObjectId;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubDbXRefs()
     */
    private Collection<PubDbXRef> getPubDbXRefs() {
        return this.pubDbXRefs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubDbXRefs(java.util.Set)
     */
    private void setPubDbXRefs(Set<PubDbXRef> pubDbXRefs) {
        this.pubDbXRefs = pubDbXRefs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeatureCvTerms()
     */
//    private Collection<FeatureCvTerm> getFeatureCvTerms() {
//        return this.featureCvTerms;
//    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeatureCvTerms(java.util.Set)
     */
    private void setFeatureCvTerms(Set<FeatureCvTerm> featureCvTerms) {
        this.featureCvTerms = featureCvTerms;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeatureRelationshipPubs()
     */
    private Collection<FeatureRelationshipPub> getFeatureRelationshipPubs() {
        return this.featureRelationshipPubs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeatureRelationshipPubs(java.util.Set)
     */
    private void setFeatureRelationshipPubs(Set<FeatureRelationshipPub> featureRelationshipPubs) {
        this.featureRelationshipPubs = featureRelationshipPubs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeaturePubs()
     */
    private Collection<FeaturePub> getFeaturePubs() {
        return this.featurePubs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeaturePubs(java.util.Set)
     */
    private void setFeaturePubs(Set<FeaturePub> featurePubs) {
        this.featurePubs = featurePubs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeaturePropPubs()
     */
    private Collection<FeaturePropPub> getFeaturePropPubs() {
        return this.featurePropPubs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeaturePropPubs(java.util.Set)
     */
    private void setFeaturePropPubs(Set<FeaturePropPub> featurePropPubs) {
        this.featurePropPubs = featurePropPubs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeatureSynonyms()
     */
    private Collection<FeatureSynonym> getFeatureSynonyms() {
        return this.featureSynonyms;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeatureSynonyms(java.util.Set)
     */
    private void setFeatureSynonyms(Set<FeatureSynonym> featureSynonyms) {
        this.featureSynonyms = featureSynonyms;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeatureCvTermPubs()
     */
//    private Collection<FeatureCvTermPub> getFeatureCvTermPubs() {
//        return this.featureCvTermPubs;
//    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeatureCvTermPubs(java.util.Set)
     */
    private void setFeatureCvTermPubs(Set<FeatureCvTermPub> featureCvTermPubs) {
        this.featureCvTermPubs = featureCvTermPubs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeatureRelationshipPropPubs()
     */
    private Collection<FeatureRelationshipPropPub> getFeatureRelationshipPropPubs() {
        return this.featureRelationshipPropPubs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeatureRelationshipPropPubs(java.util.Set)
     */
    private void setFeatureRelationshipPropPubs(Set<FeatureRelationshipPropPub> featureRelationshipPropPubs) {
        this.featureRelationshipPropPubs = featureRelationshipPropPubs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubProps()
     */
    private Collection<PubProp> getPubProps() {
        return this.pubProps;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubProps(java.util.Set)
     */
    private void setPubProps(Set<PubProp> pubProps) {
        this.pubProps = pubProps;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getPubRelationshipsForSubjectId()
     */
    private Collection<PubRelationship> getPubRelationshipsForSubjectId() {
        return this.pubRelationshipsForSubjectId;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setPubRelationshipsForSubjectId(java.util.Set)
     */
    private void setPubRelationshipsForSubjectId(Set<PubRelationship> pubRelationshipsForSubjectId) {
        this.pubRelationshipsForSubjectId = pubRelationshipsForSubjectId;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#getFeatureLocPubs()
     */
    private Collection<FeatureLocPub> getFeatureLocPubs() {
        return this.featureLocPubs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.PubI#setFeatureLocPubs(java.util.Set)
     */
    private void setFeatureLocPubs(Set<FeatureLocPub> featureLocPubs) {
        this.featureLocPubs = featureLocPubs;
    }




}


