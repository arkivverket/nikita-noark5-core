package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import javax.validation.constraints.NotNull;

public interface IMetadataSuperService {

    /**
     * retrieve a valid metadata entity identified by particular code
     * while verifying that the codename matches the code.
     * Raise exception if the code is unknown or if the code and
     * codename do not match the ones in the catalog.
     *
     * @param code The code of the object you wish to retrieve
     * @return The metadata entity object
     */
    IMetadataEntity findValidMetadataOrThrow(
        String parent, String code, String codename);

    /**
     * retrieve a valid metadata entity identified by particular code
     * while verifying that the codename matches the code.
     * Raise exception if the code is unknown or if the code and
     * codename do not match the ones in the catalog.
     *
     * @param template The values of the object you wish to retrieve
     * @return The metadata entity object
     */
    IMetadataEntity findValidMetadataOrThrow(
        String parent, IMetadataEntity template);

    /**
     * retrieve a metadata entity identified by particular code.
     * Return null if the code is unknown.
     *
     * @param code The code of the object you wish to retrieve
     * @return The metadata entity object or null.
     */
    IMetadataEntity findMetadataByCode(@NotNull String code);

    /**
     * retrieve a metadata entity identified by particular code.
     * Raise exception if the code is unknown.
     *
     * @param code The code of the object you wish to retrieve
     * @return The metadata entity object
     */
    IMetadataEntity findMetadataByCodeOrThrow(@NotNull String code);

    /**
     * retrieve a metadata entity identified by particular code.
     * Raise exception if the code is unknown.
     *
     * @param code The code of the object you wish to retrieve
     * @return A metadata object wrapped as a MetadataHateoas object
     */
    MetadataHateoas findByCode(@NotNull String code);
}
