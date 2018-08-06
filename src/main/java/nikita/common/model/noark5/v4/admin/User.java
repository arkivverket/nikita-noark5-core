package nikita.common.model.noark5.v4.admin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.interfaces.entities.admin.IUserEntity;
import nikita.common.util.deserialisers.admin.UserDeserializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static nikita.common.config.Constants.*;

@Entity
@Table(name = "nikita_user")
@AttributeOverride(name = "id",
        column = @Column(
                name = PRIMARY_KEY_USER))
@JsonDeserialize(using = UserDeserializer.class)
public class User
        extends NoarkEntity
        implements IUserEntity {

    @Column(name = "system_id", unique = true)
    @NotNull
    private String systemId;

    @Column(unique = true)
    @NotNull
    private String username;

    @Column(name = "password", length = 100)
    @NotNull
    @Size(min = 1, max = 100)
    private String password;

    @Column(name = "firstname", length = 50)
    @Size(min = 1, max = 50)
    private String firstname;

    @Column(name = "lastname", length = 50)
    @Size(min = 1, max = 50)
    private String lastname;

    /**
     * M600 - opprettetDato (xs:dateTime)
     */
    @Column(name = "account_created_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Audited
    private Date createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @Column(name = "created_by")
    @Audited
    private String createdBy;

    /**
     * M602 - avsluttetDato (xs:dateTime)
     */
    @Column(name = "finalised_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Audited
    private Date finalisedDate;

    /**
     * M603 - avsluttetAv (xs:string)
     */
    @Column(name = "finalised_by")
    @Audited
    private String finalisedBy;

    @NotNull
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @NotNull
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @NotNull
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled;

    @Column(name = "last_password_reset_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "nikita_user_authority",
            joinColumns = {@JoinColumn(name = FOREIGN_KEY_USER_PK,
                    referencedColumnName = PRIMARY_KEY_USER)},
            inverseJoinColumns = {@JoinColumn(name = "authority_id",
                    referencedColumnName = "id")})
    private List<Authority> authorities;

//
//    @OneToMany(mappedBy = "referenceUser", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<CorrespondencePartInternal>
//            referenceCorrespondencePartInternal = new ArrayList<>();

    @Override
    public String getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Date getFinalisedDate() {
        return finalisedDate;
    }

    @Override
    public void setFinalisedDate(Date finalisedDate) {
        this.finalisedDate = finalisedDate;
    }

    @Override
    public String getFinalisedBy() {
        return finalisedBy;
    }

    @Override
    public void setFinalisedBy(String finalisedBy) {
        this.finalisedBy = finalisedBy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
//
//    public List<CorrespondencePartInternal> getReferenceCorrespondencePartInternal() {
//        return referenceCorrespondencePartInternal;
//    }
//
//    public void setReferenceCorrespondencePartInternal(
//            List<CorrespondencePartInternal> referenceCorrespondencePartInternal) {
//        this.referenceCorrespondencePartInternal = referenceCorrespondencePartInternal;
//    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_ADMINISTRATION_PATH;
    }

    @Override
    public String getBaseTypeName() {
        return N5ResourceMappings.USER;
    }

    @Override
    public String toString() {
        return "User{" + super.toString() +
                "username='" + username + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", finalisedDate=" + finalisedDate +
                ", finalisedBy='" + finalisedBy + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", enabled=" + enabled +
                ", lastPasswordResetDate=" + lastPasswordResetDate +
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
        User rhs = (User) other;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(createdDate, rhs.createdDate)
                .append(createdBy, rhs.createdBy)
                .append(finalisedBy, rhs.finalisedBy)
                .append(finalisedDate, rhs.finalisedDate)
                .append(accountNonExpired, rhs.accountNonExpired)
                .append(accountNonLocked, rhs.accountNonLocked)
                .append(username, rhs.username)
                .append(password, rhs.password)
                .append(firstname, rhs.firstname)
                .append(lastname, rhs.lastname)
                .append(enabled, rhs.enabled)
                .append(lastPasswordResetDate, rhs.lastPasswordResetDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(createdDate)
                .append(createdBy)
                .append(finalisedBy)
                .append(finalisedDate)
                .append(accountNonExpired)
                .append(accountNonLocked)
                .append(username)
                .append(password)
                .append(firstname)
                .append(lastname)
                .append(enabled)
                .append(lastPasswordResetDate)
                .toHashCode();
    }
}
