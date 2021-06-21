package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.interfaces.IDocumentMedium;
import nikita.common.model.noark5.v5.interfaces.IFondsCreator;
import nikita.common.model.noark5.v5.interfaces.IStorageLocation;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.util.deserialisers.FondsDeserializer;
import nikita.webapp.hateoas.FondsHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

@Entity
@Table(name = TABLE_FONDS)
@JsonDeserialize(using = FondsDeserializer.class)
@HateoasPacker(using = FondsHateoasHandler.class)
@HateoasObject(using = FondsHateoas.class)
@Indexed
public class Fonds
        extends NoarkGeneralEntity
        implements IStorageLocation, IDocumentMedium, IFondsCreator {

    private static final long serialVersionUID = 1L;

    // Links to Series
    @OneToMany(mappedBy = REFERENCE_FONDS)
    @JsonIgnore
    private final List<Series> referenceSeries = new ArrayList<>();
    // Links to child Fonds
    @OneToMany(mappedBy = REFERENCE_PARENT_FONDS, fetch = LAZY)
    private final List<Fonds> referenceChildFonds = new ArrayList<>();
    // Links to StorageLocations
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_FONDS_STORAGE_LOCATION,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_FONDS_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<StorageLocation> referenceStorageLocation =
            new HashSet<>();
    // Links to FondsCreators
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = TABLE_FONDS_FONDS_CREATOR,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_FONDS_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_FONDS_CREATOR_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID))
    private final Set<FondsCreator> referenceFondsCreator = new HashSet<>();
    /**
     * M??? - arkivstatus code (xs:string)
     */
    @Column(name = FONDS_STATUS_CODE_ENG)
    @Audited
    @JsonProperty(FONDS_STATUS_CODE_NAME)
    private String fondsStatusCode;

    // Link to parent Fonds
    @ManyToOne(fetch = LAZY)
    private Fonds referenceParentFonds;
    /**
     * M050 - arkivstatus code name (xs:string)
     */
    @Column(name = FONDS_STATUS_CODE_NAME_ENG)
    @Audited
    @JsonProperty(FONDS_STATUS_CODE_NAME)
    private String fondsStatusCodeName;
    /**
     * M??? - dokumentmedium code (xs:string)
     */
    @Column(name = DOCUMENT_MEDIUM_CODE_ENG)
    @Audited
    @JsonProperty(DOCUMENT_MEDIUM_CODE)
    private String documentMediumCode;
    /**
     * M300 - dokumentmedium code name (xs:string)
     */
    @Column(name = DOCUMENT_MEDIUM_CODE_NAME_ENG)
    @Audited
    @JsonProperty(DOCUMENT_MEDIUM_CODE_NAME)
    private String documentMediumCodeName;

    public FondsStatus getFondsStatus() {
        if (null == fondsStatusCode)
            return null;
        return new FondsStatus(fondsStatusCode, fondsStatusCodeName);
    }

    public void setFondsStatus(FondsStatus fondsStatus) {
        if (null != fondsStatus) {
            this.fondsStatusCode = fondsStatus.getCode();
            this.fondsStatusCodeName = fondsStatus.getCodeName();
        } else {
            this.fondsStatusCode = null;
            this.fondsStatusCodeName = null;
        }
    }

    public DocumentMedium getDocumentMedium() {
        if (null == documentMediumCode)
            return null;
        return new DocumentMedium(documentMediumCode, documentMediumCodeName);
    }

    public void setDocumentMedium(DocumentMedium documentMedium) {
        if (null != documentMedium) {
            this.documentMediumCode = documentMedium.getCode();
            this.documentMediumCodeName = documentMedium.getCodeName();
        } else {
            this.documentMediumCode = null;
            this.documentMediumCodeName = null;
        }
    }

    @Override
    public String getBaseTypeName() {
        return FONDS;
    }

    @Override
    public String getBaseRel() {
        return REL_FONDS_STRUCTURE_FONDS;
    }

    public List<Series> getReferenceSeries() {
        return referenceSeries;
    }

    public void addSeries(Series series) {
        this.referenceSeries.add(series);
        series.setReferenceFonds(this);
    }

    public void removeSeries(Series series) {
        this.referenceSeries.remove(series);
        series.setReferenceFonds(null);
    }

    public Fonds getReferenceParentFonds() {
        return referenceParentFonds;
    }

    public void setReferenceParentFonds(Fonds referenceParentFonds) {
        this.referenceParentFonds = referenceParentFonds;
    }

    public List<Fonds> getReferenceChildFonds() {
        return referenceChildFonds;
    }

    public void addReferenceChildFonds(Fonds childFonds) {
        this.referenceChildFonds.add(childFonds);
        childFonds.setReferenceParentFonds(this);
    }

    public void removeReferenceChildFonds(Fonds childFonds) {
        this.referenceChildFonds.remove(childFonds);
        childFonds.setReferenceParentFonds(null);
    }

    @Override
    public Set<StorageLocation> getReferenceStorageLocation() {
        return referenceStorageLocation;
    }

    @Override
    public void addReferenceStorageLocation(StorageLocation storageLocation) {
        this.referenceStorageLocation.add(storageLocation);
        storageLocation.getReferenceFonds().add(this);
    }

    @Override
    public void removeReferenceStorageLocation(StorageLocation storageLocation) {
        this.referenceStorageLocation.remove(storageLocation);
        storageLocation.getReferenceFonds().remove(this);
    }

    @Override
    public Set<FondsCreator> getReferenceFondsCreator() {
        return referenceFondsCreator;
    }

    public void addFondsCreator(FondsCreator fondsCreator) {
        this.referenceFondsCreator.add(fondsCreator);
        fondsCreator.getReferenceFonds().add(this);
    }

    @Override
    public String toString() {
        return "Fonds{" + super.toString() +
                ", fondsStatusCode='" + fondsStatusCode + '\'' +
                ", fondsStatusCodeName='" + fondsStatusCodeName + '\'' +
                ", documentMediumCode='" + documentMediumCode + '\'' +
                ", documentMediumCodeName='" + documentMediumCodeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != getClass()) {
            return false;
        }
        Fonds rhs = (Fonds) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(fondsStatusCode, rhs.fondsStatusCode)
                .append(fondsStatusCodeName, rhs.fondsStatusCodeName)
                .append(documentMediumCode, rhs.documentMediumCode)
                .append(documentMediumCodeName, rhs.documentMediumCodeName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(fondsStatusCode)
                .append(fondsStatusCodeName)
                .append(documentMediumCode)
                .append(documentMediumCodeName)
                .toHashCode();
    }
}
