package nikita.common.model.noark5.v5.admin;

import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IRoleEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;

//@Entity
//@Table(name = TABLE_nikita_role")
public class Role
        extends SystemIdEntity
        implements IRoleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    private Long id;

    /**
     * M??? - rolle (xs:string)
     */
    @Column(name = "role")
    @Audited
    private String role;

    /**
     * M??? - tilgangskategori (xs:string)
     */
    @Column(name = N5ResourceMappings.ACCESS_CATEGORY_ENG)
    @Audited
    private String accessCategory;

    /**
     * M??? - referanseArkivenhet (xs:string)
     */
    @Column(name = "reference_entity")
    @Audited
    private String referenceEntity;

    /**
     * M??? - tilgangsrestriksjon (xs:string)
     */
    @Column(name = "access_restriction")
    @Audited
    private String accessRestriction;

    /**
     * M??? - les (xs:string)
     */
    @Column(name = "read")
    @Audited
    private Boolean read;

    /**
     * M??? - ny (xs:string)
     */
    @Column(name = "create")
    @Audited
    private Boolean create;

    /**
     * M??? - endre (xs:string)
     */
    @Column(name = "update")
    @Audited
    private Boolean update;

    /**
     * M??? - slett (xs:string)
     */
    @Column(name = "delete")
    @Audited
    private Boolean delete;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccessCategory() {
        return accessCategory;
    }

    public void setAccessCategory(String accessCategory) {
        this.accessCategory = accessCategory;
    }

    public String getReferenceEntity() {
        return referenceEntity;
    }

    public void setReferenceEntity(String referenceEntity) {
        this.referenceEntity = referenceEntity;
    }

    public String getAccessRestriction() {
        return accessRestriction;
    }

    public void setAccessRestriction(String accessRestriction) {
        this.accessRestriction = accessRestriction;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "Role{" + super.toString() +
                "role='" + role + '\'' +
                ", referenceEntity='" + referenceEntity + '\'' +
                ", read=" + read +
                ", create=" + create +
                ", update=" + update +
                ", delete=" + delete +
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
        Role rhs = (Role) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(referenceEntity, rhs.referenceEntity)
                .append(read, rhs.read)
                .append(create, rhs.create)
                .append(update, rhs.update)
                .append(delete, rhs.delete)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(referenceEntity)
                .append(read)
                .append(create)
                .append(update)
                .append(delete)
                .toHashCode();
    }
}
