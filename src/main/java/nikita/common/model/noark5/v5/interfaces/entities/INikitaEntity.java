package nikita.common.model.noark5.v5.interfaces.entities;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

public interface INikitaEntity extends Serializable {
    Long getId();

    void setId(Long id);

    String getSystemId();

    void setSystemId(String systemId);

    String getOwnedBy();

    void setOwnedBy(String ownedBy);

    Boolean getDeleted();

    void setDeleted(Boolean deleted);

    Long getVersion();

    void setVersion(Long version);

    /**
     * Tell nikita what you are. A Fonds returns "arkiv", a File "mappe" and
     * CaseFile "saksmappe". Required when building endpoint URI in Hateoas links.
     *
     * @return The name of the base type of the entity
     */
    String getBaseTypeName();

    /**
     * Tell nikita what kind of endpoint you belong to. A Fonds returns "arkivstruktur", as does
     * a File, while a CaseFile returns "sakarkiv". DocumentStatus returns "metaadata".Required when
     * building endpoint URI in Hateoas links.
     *
     * @return The name of the functional area the entity belongs to
     */
    String getFunctionalTypeName();

    ZonedDateTime getLastModifiedDate();

    String getLastModifiedBy();

    void createReference(@NotNull INikitaEntity entity,
                         @NotNull String referenceType);
}
