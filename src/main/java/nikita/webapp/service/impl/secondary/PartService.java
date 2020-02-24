package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.*;
import nikita.common.model.noark5.v5.metadata.PartRole;
import nikita.common.model.noark5.v5.secondary.Part;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.repository.n5v5.metadata.IPartRoleRepository;
import nikita.common.repository.n5v5.secondary.IPartRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.secondary.IPartService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.Constants.TEMPLATE_PART_ROLE_CODE;
import static nikita.common.config.N5ResourceMappings.*;

@Service
@Transactional
public class PartService
        extends NoarkService
        implements IPartService {

    private static final Logger logger =
            LoggerFactory.getLogger(PartService.class);

    private final IPartRoleRepository partRoleRepository;
    private final IPartRepository partRepository;
    private IMetadataService metadataService;
    private final IPartHateoasHandler partHateoasHandler;

    public PartService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IPartRoleRepository partRoleRepository,
                       IPartRepository partRepository,
                       IMetadataService metadataService,
                       IPartHateoasHandler partHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.partRoleRepository = partRoleRepository;
        this.partRepository = partRepository;
        this.metadataService = metadataService;
        this.partHateoasHandler = partHateoasHandler;
    }

    @Override
    public Part findBySystemId(@NotNull String systemId) {
        return getPartOrThrow(systemId);
    }

    /**
     * Update the PartPerson identified by systemId. Retrieve a
     * copy of the PartPerson from the database. If it does not
     * exist throw a not found exception. If it exists, call sub methods that
     * copy the values. Save the updated object back to the database.
     *
     * @param systemId     systemId of the
     *                     PartPerson to update
     * @param version      ETAG version
     * @param incomingPart incoming PartPerson with
     *                     values to copy from
     * @return the updated PartPerson
     */
    @Override
    public PartPerson
    updatePartPerson(
            @NotNull String systemId, @NotNull Long version,
            @NotNull PartPerson incomingPart) {
        PartPerson existingPart = (PartPerson) getPartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        existingPart.setdNumber(
                incomingPart.getdNumber());
        existingPart.setName(
                incomingPart.getName());
        existingPart.setSocialSecurityNumber(
                incomingPart.getSocialSecurityNumber());
        // Only copy if changed, in case it has an historical value
        if (existingPart.getPartRole() != incomingPart.getPartRole())
            existingPart.setPartRole(incomingPart.getPartRole());

        // Then secondary objects
        updatePartContactInformationCreateIfNull(
                existingPart, incomingPart);
        // Residing address
        updatePartResidingAddressCreateIfNull
                (existingPart, incomingPart);
        // Postal address
        updatePartPostalAddressCreateIfNull(
                existingPart, incomingPart);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingPart.setVersion(version);
        existingPart = partRepository.save(existingPart);
        return existingPart;
    }

    @Override
    public PartUnit updatePartUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull PartUnit incomingPart) {

        PartUnit existingPart = (PartUnit) getPartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPart.setName(
                incomingPart.getName());
        existingPart.setOrganisationNumber(
                incomingPart.getOrganisationNumber());
        existingPart.setContactPerson(
                incomingPart.getContactPerson());
        // Only copy if changed, in case it has an historical value
        if (existingPart.getPartRole() != incomingPart.getPartRole())
            existingPart.setPartRole(incomingPart.getPartRole());

        // Then secondary objects
        // Contact information
        updatePartContactInformationCreateIfNull(
                existingPart, incomingPart);
        // Business address
        updatePartUnitBusinessAddressCreateIfNull(
                existingPart, incomingPart);
        // Postal address
        updatePartPostalAddressCreateIfNull(
                existingPart, incomingPart);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingPart.setVersion(version);
        existingPart = partRepository.save(existingPart);
        return existingPart;
    }

    @Override
    public PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson part, @NotNull Record record) {

        validatePartRole(part);
        createPerson(part);
        part.addReferenceRecord(record);
        part = partRepository.save(part);
        record.addPart(part);

        PartPersonHateoas partPersonHateoas = new PartPersonHateoas(part);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));
        return partPersonHateoas;
    }

    @Override
    public PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson part, @NotNull File file) {
        validatePartRole(part);
        createPerson(part);
        part.addReferenceFile(file);
        part = partRepository.save(part);
        file.addPart(part);
        PartPersonHateoas partPersonHateoas = new PartPersonHateoas(part);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));
        return partPersonHateoas;
    }

    @Override
    public PartUnitHateoas createNewPartUnit(PartUnit part, Record record) {
        validatePartRole(part);
        createUnit(part);
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        part.addReferenceRecord(record);
        part = partRepository.save(part);
        record.addPart(part);

        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(part);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));

        return partUnitHateoas;
    }

    @Override
    public PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit part, @NotNull File file) {
        validatePartRole(part);
        createUnit(part);
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        part.addReferenceFile(file);
        part = partRepository.save(part);
        file.addPart(part);
        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(part);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));
        return partUnitHateoas;
    }

    @Override
    public PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit partUnit,
            @NotNull DocumentDescription documentDescription) {
        validatePartRole(partUnit);
        createUnit(partUnit);
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        partUnit.addReferenceDocumentDescription(documentDescription);
        partUnit = partRepository.save(partUnit);
        documentDescription.addPart(partUnit);
        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(partUnit);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, partUnit));
        return partUnitHateoas;
    }

    @Override
    public PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson partPerson,
            @NotNull DocumentDescription documentDescription) {
        validatePartRole(partPerson);
        createPerson(partPerson);
        partPerson.addReferenceDocumentDescription(documentDescription);
        partPerson = partRepository.save(partPerson);
        documentDescription.addPart(partPerson);
        PartPersonHateoas partPersonHateoas = new PartPersonHateoas(partPerson);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, partPerson));
        return partPersonHateoas;
    }

    @Override
    public void deletePartPerson(@NotNull String systemId) {
        deleteEntity(getPartOrThrow(systemId));
    }

    @Override
    public void deletePartUnit(@NotNull String systemId) {
        deleteEntity(getPartOrThrow(systemId));
    }

    // Internal helper methods

    private void createPerson(PartPerson part) {
        ContactInformation contactInformation
                = part.getContactInformation();

        if (contactInformation != null) {
            contactInformation.setPartPerson(part);
        }
        PostalAddress postalAddress = part.getPostalAddress();
        if (null != postalAddress) {
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
            postalAddress.setPartPerson(part);
        }
        ResidingAddress residingAddress = part.getResidingAddress();
        if (null != residingAddress) {
            residingAddress.getSimpleAddress().setAddressType(RESIDING_ADDRESS);
            residingAddress.setPartPerson(part);
        }
    }

    private void createUnit(PartUnit part) {
        ContactInformation contactInformation
                = part.getContactInformation();
        part.setContactInformation(contactInformation);

        // Set NikitaEntity values for ContactInformation, PostalAddress,
        // BusinessAddress
        PostalAddress postalAddress = part.getPostalAddress();
        if (null != postalAddress) {
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
            postalAddress.setPartUnit(part);
        }
        BusinessAddress businessAddress = part.getBusinessAddress();
        if (null != businessAddress) {
            businessAddress.getSimpleAddress().setAddressType(BUSINESS_ADDRESS);
            businessAddress.setPartUnit(part);
        }
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid Part back. If there
     * is no valid Part, an exception is thrown
     *
     * @param partSystemId systemId of part to
     *                     retrieve
     * @return the retrieved Part
     */
    private Part getPartOrThrow(
            @NotNull String partSystemId) {
        Part part = partRepository.findBySystemId(
                UUID.fromString(partSystemId));
        if (part == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Part, " +
                    "using systemId " + partSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return part;
    }

    /**
     * Update BusinessAddress if it exists. If none exists, create a
     * new BusinessAddress and set the values.
     *
     * @param existingPart The existing Part
     * @param incomingPart The incoming Part
     */
    public void updatePartUnitBusinessAddressCreateIfNull(
            IBusinessAddress existingPart,
            IBusinessAddress incomingPart) {
        if (null == incomingPart.getBusinessAddress()) {
            existingPart.setBusinessAddress(null);
            return;
        }
        if (null != existingPart.getBusinessAddress()
            && null != incomingPart.getBusinessAddress()) {
            updateAddress
                (existingPart.getBusinessAddress().getSimpleAddress(),
                 incomingPart.getBusinessAddress().getSimpleAddress());
        }
        // Create a new BusinessAddress object based on the incoming one
        else {
            BusinessAddress businessAddress = new BusinessAddress();
            businessAddress.setSimpleAddress(new SimpleAddress());
            updateAddress
                (businessAddress.getSimpleAddress(),
                 incomingPart.getBusinessAddress().getSimpleAddress());
            existingPart.setBusinessAddress(businessAddress);
        }
        existingPart.getBusinessAddress().getSimpleAddress().
                setAddressType(BUSINESS_ADDRESS);
    }

    /**
     * Update ResidingAddress if it exists. If none exists, create a
     * new ResidingAddress and set the values.
     *
     * @param existingPart The existing Part
     * @param incomingPart The incoming Part
     */
    public void updatePartResidingAddressCreateIfNull(
            IResidingAddress existingPart,
            IResidingAddress incomingPart) {
        if (null == incomingPart.getResidingAddress()) {
            existingPart.setResidingAddress(null);
            return;
        }
        if (null != existingPart.getResidingAddress()
            && null != incomingPart.getResidingAddress()) {
            updateAddress
                (existingPart.getResidingAddress().getSimpleAddress(),
                 incomingPart.getResidingAddress().getSimpleAddress());
        }
        // Create a new ResidingAddress object based on the incoming one
        else {
            ResidingAddress residingAddress = new ResidingAddress();
            residingAddress.setSimpleAddress(new SimpleAddress());
            updateAddress
                (residingAddress.getSimpleAddress(),
                 incomingPart.getResidingAddress().getSimpleAddress());
            existingPart.setResidingAddress(residingAddress);
        }
        // Make sure the addressType field is set
        existingPart.getResidingAddress().
                getSimpleAddress().setAddressType(RESIDING_ADDRESS);
    }

    /**
     * Update PostalAddress if it exists. If none exists, create a
     * new PostalAddress and set the values.
     *
     * @param existingPostalAddress The existing
     *                              Part
     * @param incomingPostalAddress The incoming Part
     */
    public void updatePartPostalAddressCreateIfNull(
            IPostalAddress existingPart,
            IPostalAddress incomingPart) {
        if (null == incomingPart.getPostalAddress()) {
            existingPart.setPostalAddress(null);
            return;
        }

        if (null != existingPart.getPostalAddress()
            && null != incomingPart.getPostalAddress()) {
            updateAddress
                (existingPart.getPostalAddress().getSimpleAddress(),
                 incomingPart.getPostalAddress().getSimpleAddress());
        }
        // Create a new PostalAddress object based on the incoming one
        else {
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setSimpleAddress(new SimpleAddress());
            updateAddress
                (postalAddress.getSimpleAddress(),
                 incomingPart.getPostalAddress().getSimpleAddress());
            existingPart.setPostalAddress(postalAddress);
        }
        // Make sure the addressType field is set
        existingPart.getPostalAddress().getSimpleAddress().
                setAddressType(POSTAL_ADDRESS);
    }

    /**
     * Update ContactInformation if it exists. If none exists, create a
     * new ContactInformation and set the values.
     *
     * @param existingPart The existing Part
     * @param incomingPart The incoming Part
     */
    public void updatePartContactInformationCreateIfNull(
            IContactInformation existingPart,
            IContactInformation incomingPart) {
        if (null == incomingPart.getContactInformation()) {
            existingPart.setContactInformation(null);
            return;
        }
        if (existingPart.getContactInformation() != null &&
                incomingPart.getContactInformation() != null) {
            updateContactInformation
                (existingPart.getContactInformation(),
                 incomingPart.getContactInformation());
        }
        // Create a new ContactInformation object based on the incoming one
        else if (incomingPart.getContactInformation() != null) {
            existingPart.setContactInformation(updateContactInformation
                 (new ContactInformation(),
                  incomingPart.getContactInformation()));
        }
    }

    /**
     * Generate a Default PartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the PartUnit object wrapped as a
     * PartUnitHateoas object
     */
    @Override
    public PartUnitHateoas generateDefaultPartUnit(
            final String recordSystemId) {
        PartUnit suggestedPart = new PartUnit();

        setDefaultPartRole(suggestedPart);

        PartUnitHateoas partHateoas = new PartUnitHateoas(suggestedPart);
        partHateoasHandler.addLinksOnTemplate(partHateoas, new Authorisation());
        return partHateoas;
    }

    /**
     * Generate a Default PartPerson object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the PartPerson object wrapped as a
     * PartPersonHateoas object
     */
    @Override
    public PartPersonHateoas generateDefaultPartPerson(
            final String recordSystemId) {
        PartPerson suggestedPart = new PartPerson();

        setDefaultPartRole(suggestedPart);

        PartPersonHateoas partHateoas = new PartPersonHateoas(suggestedPart);
        partHateoasHandler.addLinksOnTemplate(partHateoas, new Authorisation());
        return partHateoas;
    }

    /**
     * Copy the values you are allowed to copy from the incoming
     * contactInformation object to the existing contactInformation object
     * retrieved from the database.
     *
     * @param existingContactInformation An existing contactInformation object
     *                                   retrieved from the database
     * @param incomingContactInformation Incoming contactInformation object
     * @return The existing ContactInformation object updated with values
     */
    public ContactInformation updateContactInformation(
            IContactInformationEntity existingContactInformation,
            IContactInformationEntity incomingContactInformation) {

        existingContactInformation.setEmailAddress(
                incomingContactInformation.getEmailAddress());
        existingContactInformation.setMobileTelephoneNumber(
                incomingContactInformation.getMobileTelephoneNumber());
        existingContactInformation.setTelephoneNumber(
                incomingContactInformation.getTelephoneNumber());
        return (ContactInformation) existingContactInformation;
    }

    /**
     * Copy the values you are allowed to copy from the incoming address object
     * to the existing address object retrieved from the database.
     *
     * @param existingAddress An existing address object retrieved from the
     *                        database
     * @param incomingAddress Incoming address object
     */
    private void updateAddress(@NotNull SimpleAddress existingAddress,
                               @NotNull SimpleAddress incomingAddress) {

        existingAddress.setAddressType(incomingAddress.getAddressType());
        existingAddress.setAddressLine1(incomingAddress.getAddressLine1());
        existingAddress.setAddressLine2(incomingAddress.getAddressLine2());
        existingAddress.setAddressLine3(incomingAddress.getAddressLine3());
        existingAddress.setPostalNumber(incomingAddress.getPostalNumber());
        existingAddress.setPostalTown(incomingAddress.getPostalTown());
        existingAddress.setCountryCode(incomingAddress.getCountryCode());
    }

    private void setDefaultPartRole(@NotNull Part part) {
        PartRole partRole = (PartRole) metadataService
                .findValidMetadataByEntityTypeOrThrow(PART_ROLE,
                        TEMPLATE_PART_ROLE_CODE, null);
        part.setPartRole(partRole);
    }

    private void validatePartRole(Part part) {
        // Assume value already set, as the deserialiser will enforce it.
        PartRole partRole = (PartRole) metadataService
                .findValidMetadataByEntityTypeOrThrow(
                        PART_ROLE,
                        part.getPartRole());
        part.setPartRole(partRole);
    }
}
