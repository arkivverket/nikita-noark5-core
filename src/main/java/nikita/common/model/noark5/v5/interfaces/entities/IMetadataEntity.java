package nikita.common.model.noark5.v5.interfaces.entities;

import java.time.OffsetDateTime;

/**
 *
 */
public interface IMetadataEntity
        extends INikitaEntity {

    String getCode();

    void setCode(String code);

    String getName();

    void setName(String name);

    Boolean getInactive();

    void setInactive(Boolean inactive);

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

    OffsetDateTime getLastModifiedDate();

    String getLastModifiedBy();
}
