package nikita.common.model.noark5.v5;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.hateoas.FondsHateoas;
import nikita.common.model.noark5.v5.interfaces.IDocumentMedium;
import nikita.common.model.noark5.v5.interfaces.IFondsCreator;
import nikita.common.model.noark5.v5.interfaces.IStorageLocation;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.util.deserialisers.FondsDeserializer;
import nikita.webapp.hateoas.FondsHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.FONDS;

@Entity
@Table(name = TABLE_FONDS)
@JsonDeserialize(using = FondsDeserializer.class)
@HateoasPacker(using = FondsHateoasHandler.class)
@HateoasObject(using = FondsHateoas.class)
public class Fonds
        extends NoarkGeneralEntity
        implements IStorageLocation, IDocumentMedium, IFondsCreator {

    private static final long serialVersionUID = 1L;

    /**
     * M050 - arkivstatus (xs:string)
     */
    @Column(name = "fonds_status")
    @Audited
    private String fondsStatus;

    /**
     * M??? - dokumentmedium code (xs:string)
     */
    @Column(name = "document_medium_code")
    @Audited
    private String documentMediumCode;

    /**
     * M300 - dokumentmedium code name (xs:string)
     */
    @Column(name = "document_medium_code_name")
    @Audited
    private String documentMedium;

    // Links to Series
    @OneToMany(mappedBy = "referenceFonds")
    @JsonIgnore
    private List<Series> referenceSeries = new ArrayList<>();

    // Link to parent Fonds
    @ManyToOne(fetch = LAZY)
    private Fonds referenceParentFonds;

    // Links to child Fonds
    @OneToMany(mappedBy = "referenceParentFonds", fetch = LAZY)
    private List<Fonds> referenceChildFonds = new ArrayList<>();

    // Links to StorageLocations
    @ManyToMany(cascade = PERSIST)
    @JoinTable(name = TABLE_FONDS_STORAGE_LOCATION,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_FONDS_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_STORAGE_LOCATION_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    )
    private List<StorageLocation> referenceStorageLocation = new ArrayList<>();

    // Links to FondsCreators
    @ManyToMany
    @JoinTable(name = TABLE_FONDS_FONDS_CREATOR,
            joinColumns = @JoinColumn(
                    name = FOREIGN_KEY_FONDS_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID),
            inverseJoinColumns = @JoinColumn(
                    name = FOREIGN_KEY_FONDS_CREATOR_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID)
    )
    private List<FondsCreator> referenceFondsCreator = new ArrayList<>();

    public String getFondsStatus() {
        return fondsStatus;
    }

    public void setFondsStatus(String fondsStatus) {
        this.fondsStatus = fondsStatus;
    }

    public String getDocumentMediumCode() {
        return documentMediumCode;
    }

    public void setDocumentMediumCode(String documentMediumCode) {
        this.documentMediumCode = documentMediumCode;
    }

    public String getDocumentMediumCodeName() {
        return documentMedium;
    }

    public void setDocumentMediumCodeName(String documentMediumCodeName) {
        this.documentMedium = documentMedium;
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

    public void setReferenceSeries(List<Series> referenceSeries) {
        this.referenceSeries = referenceSeries;
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

    public void setReferenceChildFonds(List<Fonds> referenceChildFonds) {
        this.referenceChildFonds = referenceChildFonds;
    }

    public void addReferenceChildFonds(Fonds childFonds) {
        this.referenceChildFonds.add(childFonds);
    }

    @Override
    public List<StorageLocation> getReferenceStorageLocation() {
        return referenceStorageLocation;
    }

    @Override
    public void setReferenceStorageLocation(
            List<StorageLocation> referenceStorageLocation) {
        this.referenceStorageLocation = referenceStorageLocation;
    }

    @Override
    public void addReferenceStorageLocation(
            StorageLocation storageLocation) {
        this.referenceStorageLocation.add(storageLocation);
    }

    @Override
    public List<FondsCreator> getReferenceFondsCreator() {
        return referenceFondsCreator;
    }

    public void setReferenceFondsCreator(
            List<FondsCreator> referenceFondsCreator) {
        this.referenceFondsCreator = referenceFondsCreator;
    }

    @Override
    public String toString() {
        return "Fonds{" + super.toString() +
                ", fondsStatus='" + fondsStatus + '\'' +
                ", documentMedium='" + documentMedium + '\'' +
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
                .append(fondsStatus, rhs.fondsStatus)
                .append(documentMedium, rhs.documentMedium)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(fondsStatus)
                .append(documentMedium)
                .toHashCode();
    }
}
