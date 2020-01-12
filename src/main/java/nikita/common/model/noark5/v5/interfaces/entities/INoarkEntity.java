package nikita.common.model.noark5.v5.interfaces.entities;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public interface INoarkEntity
        extends Serializable, ICreate, ILastModified {

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
     * https://rel.arkivverket.no/noark5/v5/api/
     * arkivstruktur/arkiv/
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

    void createReference(@NotNull INoarkEntity entity,
                         @NotNull String referenceType);

    /***
     * Returns the identifier of the entity. For example, if it is a SystemId
     * entity, then the systemId value is returned or if it is a Metadata
     * entity the code value is returned.
     *
     * @return the identifier of the entity
     */
    String getIdentifier();

    /***
     * Returns the identifier typ of the entity. For example, if it is a
     * SystemIdentity, then 'systemId' is returned or if it is a
     * Metadata entity then 'code' is returned.
     *
     * @return the identifier type of the entity
     */
    String getIdentifierType();
}
