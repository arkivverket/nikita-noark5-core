package nikita.common.model.noark5.v5.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.admin.UserHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.interfaces.entities.admin.IUserEntity;
import nikita.common.util.deserialisers.admin.UserDeserializer;
import nikita.webapp.hateoas.admin.UserHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Entity
@Table(name = TABLE_USER)
@JsonDeserialize(using = UserDeserializer.class)
@HateoasPacker(using = UserHateoasHandler.class)
@HateoasObject(using = UserHateoas.class)
public class User
        extends SystemIdEntity
        implements IUserEntity {

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
    @Column(name = CREATED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(CREATED_DATE)
    private OffsetDateTime createdDate;

    /**
     * M601 - opprettetAv (xs:string)
     */
    @Column(name = CREATED_BY_ENG)
    @Audited
    @JsonProperty(CREATED_BY)
    private String createdBy;

    /**
     * M602 - avsluttetDato (xs:dateTime)
     */
    @Column(name = FINALISED_DATE_ENG)
    @DateTimeFormat(iso = DATE_TIME)
    @Audited
    @JsonProperty(FINALISED_DATE)
    private OffsetDateTime finalisedDate;

    /**
     * M603 - avsluttetAv (xs:string)
     */
    @Column(name = FINALISED_BY_ENG)
    @Audited
    @JsonProperty(FINALISED_BY)
    private String finalisedBy;

    @NotNull
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    @NotNull
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    @NotNull
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Column(name = "enabled")
    @NotNull
    private Boolean enabled = true;

    @Column(name = "last_password_reset_date")
    @DateTimeFormat(iso = DATE_TIME)
    private OffsetDateTime lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = TABLE_USER_AUTHORITY,
            joinColumns = {@JoinColumn(name = FOREIGN_KEY_USER_PK,
                    referencedColumnName = PRIMARY_KEY_SYSTEM_ID)},
            inverseJoinColumns = {@JoinColumn(name = "authority_id",
                    referencedColumnName = "id")})
    private List<Authority> authorities = new ArrayList<>();


    @ManyToMany(mappedBy = "users")
    private List<AdministrativeUnit> administrativeUnits = new ArrayList<>();

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public OffsetDateTime getFinalisedDate() {
        return finalisedDate;
    }

    @Override
    public void setFinalisedDate(OffsetDateTime finalisedDate) {
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

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
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

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public List<AdministrativeUnit> getAdministrativeUnits() {
        return administrativeUnits;
    }

    public void setAdministrativeUnits(List<AdministrativeUnit> administrativeUnits) {
        this.administrativeUnits = administrativeUnits;
    }

    public void addAdministrativeUnit(AdministrativeUnit administrativeUnit) {
        this.administrativeUnits.add(administrativeUnit);
    }

    public OffsetDateTime getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(OffsetDateTime lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public String getFunctionalTypeName() {
        return NOARK_ADMINISTRATION_PATH;
    }

    @Override
    public String getBaseTypeName() {
        return USER;
    }

    @Override
    public String getBaseRel() {
        return REL_ADMIN_USER;
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
