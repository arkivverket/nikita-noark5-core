package nikita.common.model.noark5.v5.interfaces.entities;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public interface INikitaEntity
        extends Serializable, INoarkCreateEntity {

    String getSystemId();

    void setSystemId(UUID systemId);

    String getOwnedBy();

    void setOwnedBy(String ownedBy);

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
     * Tell nikita what you are. A Fonds returns
     * <p>
     * https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/
     * <p>
     * Required when building endpoint URI in Hateoas links.
     *
     * @return The name of the base type of the entity
     */
    String getBaseRel();

    /**
     * Tell nikita what kind of endpoint you belong to. A Fonds returns "arkivstruktur", as does
     * a File, while a CaseFile returns "sakarkiv". DocumentStatus returns "metaadata".Required when
     * building endpoint URI in Hateoas links.
     *
     * @return The name of the functional area the entity belongs to
     */
    String getFunctionalTypeName();


    OffsetDateTime getLastModifiedDate();

    String getLastModifiedBy();

    void createReference(@NotNull INikitaEntity entity,
                         @NotNull String referenceType);
}
