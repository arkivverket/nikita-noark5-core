package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.*;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.hateoas.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.PartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.*;
import nikita.common.model.noark5.v5.metadata.PartRole;
import nikita.common.repository.n5v5.IPartRepository;
import nikita.common.repository.n5v5.metadata.IPartRoleRepository;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IPartHateoasHandler;
import nikita.webapp.security.Authorisation;
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
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
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
    private final IPartHateoasHandler partHateoasHandler;

    public PartService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IPartRoleRepository partRoleRepository,
                       IPartRepository partRepository,
                       IPartHateoasHandler partHateoasHandler) {
        super(entityManager, applicationEventPublisher);
        this.partRoleRepository = partRoleRepository;
        this.partRepository = partRepository;
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
        PartPerson existingPart =
                (PartPerson)
                        getPartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPart.setdNumber(
                incomingPart.getdNumber());
        existingPart.setName(
                incomingPart.getName());
        existingPart.setSocialSecurityNumber(
                incomingPart.getSocialSecurityNumber());

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
        partRepository.save(existingPart);
        return existingPart;
    }

    @Override
    public PartUnit updatePartUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull PartUnit incomingPart) {
        PartUnit existingPart =
                (PartUnit) getPartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPart.setName(
                incomingPart.getName());
        existingPart.setOrganisationNumber(
                incomingPart.getOrganisationNumber());
        existingPart.setContactPerson(
                incomingPart.getContactPerson());

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
        partRepository.save(existingPart);
        return existingPart;
    }

    @Override
    public PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson part, @NotNull Record record) {

        setPartRole(part);
        createPerson(part);
        record.addPartPerson(part);
        part.addRecord(record);
        partRepository.save(part);

        PartPersonHateoas partPersonHateoas = new PartPersonHateoas(part);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));
        return partPersonHateoas;
    }

    @Override
    public PartPersonHateoas createNewPartPerson(
            @NotNull PartPerson part, @NotNull File file) {
        setPartRole(part);
        createPerson(part);
        file.addPart(part);
        part.addReferenceFile(file);
        partRepository.save(part);
        PartPersonHateoas partPersonHateoas = new PartPersonHateoas(part);
        partHateoasHandler.addLinks(partPersonHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));
        return partPersonHateoas;
    }

    @Override
    public PartUnitHateoas createNewPartUnit(PartUnit part, Record record) {
        setPartRole(part);
        createUnit(part);
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        record.getReferencePartUnit().add(part);
        part.getReferenceRecord().add(record);
        partRepository.save(part);

        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(part);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));

        return partUnitHateoas;
    }

    @Override
    public PartUnitHateoas createNewPartUnit(
            @NotNull PartUnit part, @NotNull File file) {
        setPartRole(part);
        createUnit(part);
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        file.addPart(part);
        part.addReferenceFile(file);
        partRepository.save(part);
        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(part);
        partHateoasHandler.addLinks(partUnitHateoas, new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this, part));
        return partUnitHateoas;
    }

    @Override
    public void deletePartPerson(@NotNull String systemId) {
        partRepository.delete(getPartOrThrow(systemId));
    }

    @Override
    public void deletePartUnit(@NotNull String systemId) {
        partRepository.delete(getPartOrThrow(systemId));
    }

    // Internal helper methods

    private void createPerson(IGenericPersonEntity part) {
        ContactInformation contactInformation
                = part.getContactInformation();
        part.setContactInformation(contactInformation);

        PostalAddress postalAddress = part.getPostalAddress();
        if (null != postalAddress) {
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
        }
        part.setPostalAddress(postalAddress);

        ResidingAddress residingAddress =
                part.getResidingAddress();
        if (null != residingAddress) {
            residingAddress.getSimpleAddress().setAddressType(RESIDING_ADDRESS);
        }
        part.setResidingAddress(residingAddress);

    }

    private void createUnit(IGenericUnitEntity part) {
        // Set NikitaEntity values for ContactInformation, PostalAddress,
        // BusinessAddress
        PostalAddress postalAddress = part.getPostalAddress();
        if (null != postalAddress) {
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
        }
        part.setPostalAddress(postalAddress);

        BusinessAddress businessAddress =
                part.getBusinessAddress();
        if (null != businessAddress) {
            businessAddress.getSimpleAddress().setAddressType(BUSINESS_ADDRESS);
        }
        part.setBusinessAddress(businessAddress);
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

        if (existingPart.getBusinessAddress() != null &&
                incomingPart.getBusinessAddress() != null) {
            updateAddress(existingPart.
                            getBusinessAddress().getSimpleAddress(),
                    incomingPart.
                            getBusinessAddress().getSimpleAddress());
        }
        // Create a new BusinessAddress object based on the incoming one
        else if (incomingPart.getBusinessAddress() != null) {
            BusinessAddress businessAddress = new BusinessAddress();
            businessAddress.setSimpleAddress(new SimpleAddress());
            updateAddress(businessAddress.getSimpleAddress(),
                    incomingPart.getBusinessAddress()
                            .getSimpleAddress());
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

        if (existingPart.getResidingAddress() != null &&
                incomingPart.getResidingAddress() != null) {
            updateAddress(existingPart.
                            getResidingAddress().getSimpleAddress(),
                    incomingPart.
                            getResidingAddress().getSimpleAddress());
        }
        // Create a new ResidingAddress object based on the incoming one
        else if (incomingPart.getResidingAddress() != null) {
            ResidingAddress residingAddress = new ResidingAddress();
            residingAddress.setSimpleAddress(new SimpleAddress());
            updateAddress(residingAddress.getSimpleAddress(),
                    incomingPart.getResidingAddress()
                            .getSimpleAddress());
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
            IPostalAddress existingPostalAddress,
            IPostalAddress incomingPostalAddress) {

        if (existingPostalAddress.getPostalAddress() != null
                && incomingPostalAddress.getPostalAddress() != null) {
            updateAddress(existingPostalAddress.
                            getPostalAddress().getSimpleAddress(),
                    incomingPostalAddress.
                            getPostalAddress().getSimpleAddress());
        }
        // Create a new PostalAddress object based on the incoming one
        else if (incomingPostalAddress.getPostalAddress() != null) {
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setSimpleAddress(new SimpleAddress());
            updateAddress(postalAddress.getSimpleAddress(),
                    incomingPostalAddress.getPostalAddress().
                            getSimpleAddress());
            existingPostalAddress.setPostalAddress(postalAddress);
        }
        // Make sure the addressType field is set
        existingPostalAddress.getPostalAddress().getSimpleAddress().
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

        if (existingPart.getContactInformation() != null &&
                incomingPart.getContactInformation() != null) {
            updateContactInformation(existingPart.
                            getContactInformation(),
                    incomingPart.
                            getContactInformation());
        }
        // Create a new ContactInformation object based on the incoming one
        else if (incomingPart.getContactInformation() != null) {
            existingPart.setContactInformation(
                    updateContactInformation(new ContactInformation(),
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
        PartUnit suggestedPart =
                new PartUnit();

        findAndSetPartRole(CORRESPONDENCE_PART_CODE_EA,
                suggestedPart);

        createTemplatePostalAddress(suggestedPart);
        createTemplateBusinessAddress(suggestedPart);
        createTemplateContactInformation(suggestedPart);
        suggestedPart.setContactPerson("Frank Grimes");

        PartUnitHateoas partHateoas =
                new PartUnitHateoas(suggestedPart);
        partHateoasHandler.addLinksOnTemplate(
                partHateoas, new Authorisation());
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
    public PartPersonHateoas
    generateDefaultPartPerson(final String recordSystemId) {
        PartPerson suggestedPart =
                new PartPerson();

        findAndSetPartRole(CORRESPONDENCE_PART_CODE_EA,
                suggestedPart);

        createTemplatePostalAddress(suggestedPart);
        createTemplateResidingAddress(suggestedPart);
        createTemplateContactInformation(suggestedPart);
        suggestedPart.setName("Frank Grimes");
        suggestedPart.setSocialSecurityNumber("01010012345");
        suggestedPart.setdNumber("01010012345");

        PartPersonHateoas partHateoas =
                new PartPersonHateoas(suggestedPart);
        partHateoasHandler.addLinksOnTemplate(
                partHateoas, new Authorisation());
        return partHateoas;
    }

    /**
     * Create a templated ContactInformation object
     *
     * @param part templated part
     */
    private void createTemplateContactInformation(final IContactInformation
                                                          part) {
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setEmailAddress("nikita@example.com");
        contactInformation.setMobileTelephoneNumber("123456789");
        contactInformation.setTelephoneNumber("987654321");
        part.setContactInformation(contactInformation);
    }

    /**
     * Create a templated address for the business address object
     *
     * @param part templated part
     */
    private void createTemplateBusinessAddress(IBusinessAddress
                                                       part) {
        BusinessAddress businessAddress = new BusinessAddress();
        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setAddressType(BUSINESS_ADDRESS);
        simpleAddress.setAddressLine1("ADRL1 Business : 742 Evergreen Terrace");
        simpleAddress.setAddressLine2("ADRL2 Business : 742 Evergreen Terrace");
        simpleAddress.setAddressLine3("ADRL3 Business : 742 Evergreen Terrace");
        simpleAddress.setCountryCode("US");
        simpleAddress.setPostalNumber(new PostalNumber("12345"));
        simpleAddress.setPostalTown("Springfield");
        businessAddress.setSimpleAddress(simpleAddress);
        part.setBusinessAddress(businessAddress);
    }


    /**
     * Create a templated address for the residing address object
     *
     * @param part templated part
     */
    private void createTemplateResidingAddress(final IResidingAddress
                                                       part) {
        ResidingAddress residingAddress = new ResidingAddress();
        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setAddressType(RESIDING_ADDRESS);
        simpleAddress.setAddressLine1("ADRL1 Residing : 742 Evergreen Terrace");
        simpleAddress.setAddressLine2("ADRL2 Residing : 742 Evergreen Terrace");
        simpleAddress.setAddressLine3("ADRL3 Residing : 742 Evergreen Terrace");
        simpleAddress.setCountryCode("US");
        simpleAddress.setPostalNumber(new PostalNumber("12345"));
        simpleAddress.setPostalTown("Springfield");
        residingAddress.setSimpleAddress(simpleAddress);
        part.setResidingAddress(residingAddress);
    }

    /**
     * Create a templated address for the postal address object
     *
     * @param part templated part
     */
    private void createTemplatePostalAddress(IPostalAddress
                                                     part) {
        PostalAddress postalAddress = new PostalAddress();
        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setAddressType(POSTAL_ADDRESS);
        simpleAddress.setAddressLine1("ADRL1 Postal: 742 Evergreen Terrace");
        simpleAddress.setAddressLine2("ADRL2 Postal: 742 Evergreen Terrace");
        simpleAddress.setAddressLine3("ADRL3 Postal: 742 Evergreen Terrace");
        simpleAddress.setCountryCode("US");
        simpleAddress.setPostalNumber(new PostalNumber("12345"));
        simpleAddress.setPostalTown("Springfield");
        postalAddress.setSimpleAddress(simpleAddress);
        part.setPostalAddress(postalAddress);
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

    /**
     * Internal helper method to retrieve a partRole object
     * given a code e.g. EA returns EA, Avsender. Note if this does not find a
     * code it throws an exception. So this should only be called as part of
     * a process where that role of action is acceptable.
     *
     * @param code A code identifying a partRole
     * @param part The correct partRole
     */
    private void findAndSetPartRole(
            String code, Part part) {
        PartRole partRole = partRoleRepository.findByCode(code);
        if (partRole == null) {
            throw new NikitaException("Internal error, metadata missing. [" +
                    CORRESPONDENCE_PART_CODE_EA + "] returns no value");
        }
        part.setPartRole(partRole);
    }

    /**
     * The incoming PartRole does not have @id field set.
     * Therefore, we have to look it up in the database and make sure the
     * correct PartRole is associated with the Part
     * <p>
     * If PartRole type is not set, a
     * NikitaMalformedInputDataException is thrown. This means that this
     * method should originate based on a incoming request from a
     * Controller.
     *
     * @param part Incoming part
     */
    private void setPartRole(
            @NotNull Part part) {
        PartRole incomingPartRole =
                part.getPartRole();

        if (incomingPartRole != null &&
                incomingPartRole.getCode() != null) {
            PartRole partRole = partRoleRepository.findByCode(
                    incomingPartRole.getCode());
            if (partRole == null) {
                throw new NikitaMalformedInputDataException("Missing required " +
                        "PartRole value");
            }
            part.setPartRole(partRole);
        }
    }
}


