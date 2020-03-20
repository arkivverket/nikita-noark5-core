package nikita.common.model.noark5.v5.metadata;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static nikita.common.config.Constants.REL_METADATA_POSTAL_NUMBER;
import static nikita.common.config.Constants.TABLE_POSTAL_CODE;
import static nikita.common.config.N5ResourceMappings.POSTAL_NUMBER;

// Noark 5v5 postnummer
@Entity
@Table(name = TABLE_POSTAL_CODE)
public class PostalCode
        extends Metadata {

    private static final long serialVersionUID = 1L;

    // TODO drop PostalCode specific values?
    /**
     * Kommunenummer (xs:string)
     */
    @Column(name = "municipality_number")
    @Audited
    protected String municipalitynumber;

    /**
     * Kommunenavn (xs:string")
     */
    @Column(name = "municipality_name")
    @Audited
    protected String municipalityname;

    /**
     * kategori  (xs:string")
     */
    @Column(name = "category")
    @Audited
    protected String category;

    public PostalCode() {
    }

    public PostalCode(String code, String codename) {
        super(code, codename);
    }

    public PostalCode(String code) {
        super(code, (String)null);
    }

    public String getMunicipalitynumber() {
        return municipalitynumber;
    }

    public void setMunicipalitynumber(String municipalitynumber) {
        this.municipalitynumber = municipalitynumber;
    }

    public String getMunicipalityname() {
        return municipalityname;
    }

    public void setMunicipalityname(String municipalityname) {
        this.municipalityname = municipalityname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getBaseTypeName() {
        return POSTAL_NUMBER;
    }

    @Override
    public String getBaseRel() {
        return REL_METADATA_POSTAL_NUMBER;
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
        PostalCode rhs = (PostalCode) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(municipalitynumber, rhs.municipalitynumber)
                .append(municipalityname, rhs.municipalityname)
                .append(category, rhs.category)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(municipalityname)
                .append(municipalitynumber)
                .append(category)
                .toHashCode();
    }
}
