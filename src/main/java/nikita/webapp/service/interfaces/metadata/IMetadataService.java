package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.Metadata;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;

public interface IMetadataService {

    /**
     * Delete a metadata object identified by Code
     *
     * @param code The code of the metadata object to delete
     * @return empty response, but 206 No contet
     */
    ResponseEntity<String> deleteMetadataEntity(@NotNull final String code);

    /**
     * Find a valid metadata object by entity type and check that the (code,
     * codename) pair are correct. Note this method is marked as deprecated
     * as we wish to move a MetadataEntity object around, not two strings.
     *
     * @param entityType the type of Metadata object e.g. tilgangsrestriksjon
     * @param code       The code value of the Metadata object e.g. B
     * @param codename   The codename value of the Metadata object e.g.
     *                   'Begrenset etter sikkerhetsinstruksen'
     * @return The Metadata object corresponding to the code
     */
    @Deprecated
    IMetadataEntity findValidMetadataByEntityTypeOrThrow(
            @NotNull final String entityType, @NotNull final String code,
            @NotNull final String codename);

    /**
     * Check that the (code, codename) pair are correct. Given a particular
     * code, make sure that the associated codename is valid.
     * <p>
     * NikitaMalformedInputDataException (400 Bad Request) is thrown if the
     * code is null or the two codename values do not match.
     * <p>
     *
     * @param entityType     the type of Metadata object e.g. tilgangsrestriksjon
     * @param metadataEntity the Metadata object e.g. B
     *                       '('B', Begrenset etter sikkerhetsinstruksen')
     * @return The Metadata object corresponding to the code
     */
    IMetadataEntity findValidMetadataByEntityTypeOrThrow(
            @NotNull final String entityType,
            @NotNull final IMetadataEntity metadataEntity);

    /**
     * @param entityType The type of entity you wish to retrieve
     *                   e.g. tilgangsrestriksjon
     * @param code       The code of the object you wish to retrieve
     * @return The Metadata object corresponding to the code
     */
    IMetadataEntity
    findMetadataByCodeOrThrow(@NotNull final String entityType,
                              @NotNull final String code);

    /**
     * Find a given Metadata object identified by code and pack it as a
     * MetadataHateoas object. It uses the entity type that is derived from
     * the request associated with the current thread is used to get the
     * correct repository.
     *
     * @param code The code of the Metadata object to retrieve
     * @return the metadata object packed as a MetadataHateoas object
     */
    ResponseEntity<MetadataHateoas>
    findMetadataByCodeOrThrow(@NotNull final String code);

    /**
     * retrieve a list of all IMetadataEntity
     *
     * @return A list of IMetadataEntity wrapped as a MetadataHateoas object,
     * put inside a ResponseEntity that has all HTTP values set.
     */
    ResponseEntity<MetadataHateoas> findAll();

    ResponseEntity<String> generateTemplateMetadata();

    /**
     * Update an existing metadata object.  Raise exception if the code is
     * unknown.
     *
     * @param code             The code of the incoming metadata object
     * @param incomingMetadata Incoming MetadataEntity object to update
     * @return The metadata object wrapped as a MetadataHateoas object after
     * has been persisted to the database
     */
    ResponseEntity<MetadataHateoas> updateMetadataEntity(
            @NotNull final String code,
            @NotNull final Metadata incomingMetadata);

    /**
     * Create a new IMetadataEntity and persist it to the database
     *
     * @param entity Incoming IMetadataEntity object to persist
     * @return The metadata object wrapped as a MetadataHateoas object after
     * has been persisted to the database
     */
    ResponseEntity<MetadataHateoas> createNewMetadataEntity(
            @NotNull final IMetadataEntity entity)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException;
}
