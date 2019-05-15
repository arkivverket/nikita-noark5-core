package nikita.webapp.service.impl.secondary;

import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.casehandling.secondary.*;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.*;
import nikita.common.model.noark5.v4.metadata.CorrespondencePartType;
import nikita.common.repository.n5v4.metadata.ICorrespondencePartTypeRepository;
import nikita.common.repository.n5v4.secondary.ICorrespondencePartRepository;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.ICorrespondencePartTypeService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setNikitaEntityValues;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.setSystemIdEntityValues;

@Service
@Transactional
public class CorrespondencePartService
        extends NoarkService
        implements ICorrespondencePartService {

    private static final Logger logger =
            LoggerFactory.getLogger(CorrespondencePartService.class);

    private final ICorrespondencePartRepository correspondencePartRepository;
    private final ICorrespondencePartTypeRepository
            correspondencePartTypeRepository;
    private final ICorrespondencePartHateoasHandler
            correspondencePartHateoasHandler;
    private final ICorrespondencePartTypeService correspondencePartTypeService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CorrespondencePartService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            ICorrespondencePartRepository correspondencePartRepository,
            ICorrespondencePartTypeRepository correspondencePartTypeRepository,
            ICorrespondencePartHateoasHandler correspondencePartHateoasHandler,
            ICorrespondencePartTypeService correspondencePartTypeService) {
        super(entityManager, applicationEventPublisher);
        this.correspondencePartRepository = correspondencePartRepository;
        this.correspondencePartTypeRepository = correspondencePartTypeRepository;
        this.correspondencePartHateoasHandler = correspondencePartHateoasHandler;
        this.correspondencePartTypeService = correspondencePartTypeService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public CorrespondencePart findBySystemId(@NotNull String systemId) {
        return getCorrespondencePartOrThrow(systemId);
    }

    /**
     * Update the CorrespondencePartPerson identified by systemId. Retrieve a
     * copy of the CorrespondencePartPerson from the database. If it does not
     * exist throw a not found exception. If it exists, call sub methods that
     * copy the values. Save the updated object back to the database.
     *
     * @param systemId                   systemId of the
     *                                   CorrespondencePartPerson to update
     * @param version                    ETAG version
     * @param incomingCorrespondencePart incoming CorrespondencePartPerson with
     *                                   values to copy from
     * @return the updated CorrespondencePartPerson
     */
    @Override
    public CorrespondencePartPerson
    updateCorrespondencePartPerson(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartPerson incomingCorrespondencePart) {
        CorrespondencePartPerson existingCorrespondencePart =
                (CorrespondencePartPerson)
                        getCorrespondencePartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingCorrespondencePart.setdNumber(
                incomingCorrespondencePart.getdNumber());
        existingCorrespondencePart.setName(
                incomingCorrespondencePart.getName());
        existingCorrespondencePart.setSocialSecurityNumber(
                incomingCorrespondencePart.getSocialSecurityNumber());

        // Then secondary objects
        updateCorrespondencePartContactInformationCreateIfNull(
                existingCorrespondencePart, incomingCorrespondencePart);
        // Residing address
        updateCorrespondencePartResidingAddressCreateIfNull
                (existingCorrespondencePart, incomingCorrespondencePart);
        // Postal address
        updateCorrespondencePartPostalAddressCreateIfNull(
                existingCorrespondencePart, incomingCorrespondencePart);

        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCorrespondencePart.setVersion(version);
        correspondencePartRepository.save(existingCorrespondencePart);
        return existingCorrespondencePart;
    }

    @Override
    public CorrespondencePartInternal updateCorrespondencePartInternal(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartInternal incomingCorrespondencePart) {
        CorrespondencePartInternal existingCorrespondencePart =
                (CorrespondencePartInternal)
                        getCorrespondencePartOrThrow(systemId);
        // Copy all the values you are allowed to copy ....

        existingCorrespondencePart.setAdministrativeUnit(
                incomingCorrespondencePart.getAdministrativeUnit());
        existingCorrespondencePart.setCaseHandler(
                incomingCorrespondencePart.getCaseHandler());
        //existingCorrespondencePart.setReferenceAdministrativeUnit
        //      (incomingCorrespondencePart.getReferenceAdministrativeUnit());
//        existingCorrespondencePart.setReferenceUser(incomingCorrespondencePart
//                .getReferenceUser());
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCorrespondencePart.setVersion(version);
        correspondencePartRepository.save(existingCorrespondencePart);
        return existingCorrespondencePart;
    }

    @Override
    public CorrespondencePartUnit updateCorrespondencePartUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartUnit incomingCorrespondencePart) {
        CorrespondencePartUnit existingCorrespondencePart =
                (CorrespondencePartUnit) getCorrespondencePartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingCorrespondencePart.setName(
                incomingCorrespondencePart.getName());
        existingCorrespondencePart.setOrganisationNumber(
                incomingCorrespondencePart.getOrganisationNumber());
        existingCorrespondencePart.setContactPerson(
                incomingCorrespondencePart.getContactPerson());

        // Then secondary objects
        // Contact information
        updateCorrespondencePartContactInformationCreateIfNull(
                existingCorrespondencePart, incomingCorrespondencePart);
        // Business address
        updateCorrespondencePartUnitBusinessAddressCreateIfNull(
                existingCorrespondencePart, incomingCorrespondencePart);
        // Postal address
        updateCorrespondencePartPostalAddressCreateIfNull(
                existingCorrespondencePart, incomingCorrespondencePart);
        // Note setVersion can potentially result in a NoarkConcurrencyException
        // exception as it checks the ETAG value
        existingCorrespondencePart.setVersion(version);
        correspondencePartRepository.save(existingCorrespondencePart);
        return existingCorrespondencePart;
    }

    @Override
    public CorrespondencePartPersonHateoas
    createNewCorrespondencePartPerson(
            CorrespondencePartPerson correspondencePart,
            RegistryEntry registryEntry) {

        setCorrespondencePartType(correspondencePart);

        ContactInformation contactInformation
                = correspondencePart.getContactInformation();
        if (null != contactInformation) {
            setNikitaEntityValues(contactInformation);
            setSystemIdEntityValues(contactInformation);
        }
        correspondencePart.setContactInformation(contactInformation);

        PostalAddress postalAddress = correspondencePart.getPostalAddress();
        if (null != postalAddress) {
            setNikitaEntityValues(postalAddress);
            setSystemIdEntityValues(postalAddress);
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
        }
        correspondencePart.setPostalAddress(postalAddress);

        ResidingAddress residingAddress =
                correspondencePart.getResidingAddress();
        if (null != residingAddress) {
            setNikitaEntityValues(residingAddress);
            setSystemIdEntityValues(residingAddress);
            residingAddress.getSimpleAddress().setAddressType(RESIDING_ADDRESS);
        }
        correspondencePart.setResidingAddress(residingAddress);

        setNikitaEntityValues(correspondencePart);
        setSystemIdEntityValues(correspondencePart);

        registryEntry.getReferenceCorrespondencePartPerson().
                add(correspondencePart);
        correspondencePart.addRegistryEntry(registryEntry);

        correspondencePartRepository.save(correspondencePart);

        CorrespondencePartPersonHateoas correspondencePartPersonHateoas =
                new CorrespondencePartPersonHateoas(correspondencePart);
        correspondencePartHateoasHandler.addLinks(
                correspondencePartPersonHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this,
                        correspondencePart));

        return correspondencePartPersonHateoas;
    }

    @Override
    public CorrespondencePartUnitHateoas createNewCorrespondencePartUnit(
            CorrespondencePartUnit correspondencePart,
            RegistryEntry registryEntry) {

        setCorrespondencePartType(correspondencePart);

        // Set NikitaEntity values for ContactInformation, PostalAddress,
        // BusinessAddress
        ContactInformation contactInformation =
                correspondencePart.getContactInformation();
        if (null != contactInformation) {
            setNikitaEntityValues(contactInformation);
            setSystemIdEntityValues(contactInformation);
        }

        PostalAddress postalAddress = correspondencePart.getPostalAddress();
        if (null != postalAddress) {
            setNikitaEntityValues(postalAddress);
            setSystemIdEntityValues(postalAddress);
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
            correspondencePart.setPostalAddress(postalAddress);
            postalAddress.setCorrespondencePartUnit(correspondencePart);
        }

        BusinessAddress businessAddress =
                correspondencePart.getBusinessAddress();
        if (null != businessAddress) {
            setNikitaEntityValues(businessAddress);
            setSystemIdEntityValues(businessAddress);
            businessAddress.getSimpleAddress().setAddressType(BUSINESS_ADDRESS);
            correspondencePart.setBusinessAddress(businessAddress);
            businessAddress.setCorrespondencePartUnit(correspondencePart);
        }

        setNikitaEntityValues(correspondencePart);
        setSystemIdEntityValues(correspondencePart);
        // bidirectional relationship @ManyToMany, set both sides of
        // relationship
        registryEntry.
                getReferenceCorrespondencePartUnit().add(correspondencePart);
        correspondencePart.getReferenceRegistryEntry().add(registryEntry);

        correspondencePartRepository.save(correspondencePart);

        CorrespondencePartUnitHateoas correspondencePartUnitHateoas =
                new CorrespondencePartUnitHateoas(correspondencePart);
        correspondencePartHateoasHandler.addLinks(correspondencePartUnitHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this,
                        correspondencePart));

        return correspondencePartUnitHateoas;

    }

    @Override
    public CorrespondencePartInternalHateoas
    createNewCorrespondencePartInternal(
            CorrespondencePartInternal correspondencePart,
            RegistryEntry registryEntry) {

        setCorrespondencePartType(correspondencePart);

        registryEntry.getReferenceCorrespondencePartInternal().
                add(correspondencePart);

        correspondencePartRepository.save(correspondencePart);

        CorrespondencePartInternalHateoas correspondencePartInternalHateoas =
                new CorrespondencePartInternalHateoas(correspondencePart);
        correspondencePartHateoasHandler.addLinks(
                correspondencePartInternalHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this,
                        correspondencePart));

        return correspondencePartInternalHateoas;
    }

    @Override
    public void deleteCorrespondencePartUnit(@NotNull String systemId) {
        correspondencePartRepository.delete(
                getCorrespondencePartOrThrow(systemId));
    }


    @Override
    public void deleteCorrespondencePartPerson(@NotNull String systemId) {
        correspondencePartRepository.delete(
                getCorrespondencePartOrThrow(systemId));
    }


    /**
     * Delete a CorrespondencePart identified by the given systemId
     * <p>
     * Note. This assumes all children have also been deleted.
     *
     * @param systemId The systemId of the CorrespondencePart object you wish
     *                 to delete
     */
    @Override
    public void deleteCorrespondencePartInternal(@NotNull String systemId) {
        correspondencePartRepository.delete(
                getCorrespondencePartOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     *
     * @return the number of objects deleted
     */
    @Override
    public long deleteAllByOwnedBy() {
        return correspondencePartRepository.deleteByOwnedBy(getUser());
    }
    // Internal helper methods

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CorrespondencePart back. If there
     * is no valid CorrespondencePart, an exception is thrown
     *
     * @param correspondencePartSystemId systemId of correspondencePart to
     *                                   retrieve
     * @return the retrieved CorrespondencePart
     */
    private CorrespondencePart getCorrespondencePartOrThrow(
            @NotNull String correspondencePartSystemId) {
        CorrespondencePart correspondencePart =
                correspondencePartRepository.findBySystemId(
                        correspondencePartSystemId);
        if (correspondencePart == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " CorrespondencePart, " +
                    "using systemId " + correspondencePartSystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return correspondencePart;
    }

    /**
     * Update BusinessAddress if it exists. If none exists, create a
     * new BusinessAddress and set the values.
     *
     * @param existingCorrespondencePart The existing CorrespondencePart
     * @param incomingCorrespondencePart The incoming CorrespondencePart
     */
    public void updateCorrespondencePartUnitBusinessAddressCreateIfNull(
            IBusinessAddress existingCorrespondencePart,
            IBusinessAddress incomingCorrespondencePart) {

        if (existingCorrespondencePart.getBusinessAddress() != null &&
                incomingCorrespondencePart.getBusinessAddress() != null) {
            updateAddress(existingCorrespondencePart.
                            getBusinessAddress().getSimpleAddress(),
                    incomingCorrespondencePart.
                            getBusinessAddress().getSimpleAddress());
        }
        // Create a new BusinessAddress object based on the incoming one
        else if (incomingCorrespondencePart.getBusinessAddress() != null) {
            BusinessAddress businessAddress = new BusinessAddress();
            businessAddress.setSimpleAddress(new SimpleAddress());
            updateAddress(businessAddress.getSimpleAddress(),
                    incomingCorrespondencePart.getBusinessAddress()
                            .getSimpleAddress());
            existingCorrespondencePart.setBusinessAddress(businessAddress);
        }
        existingCorrespondencePart.getBusinessAddress().getSimpleAddress().
                setAddressType(BUSINESS_ADDRESS);
    }

    /**
     * Update ResidingAddress if it exists. If none exists, create a
     * new ResidingAddress and set the values.
     *
     * @param existingCorrespondencePart The existing CorrespondencePart
     * @param incomingCorrespondencePart The incoming CorrespondencePart
     */
    public void updateCorrespondencePartResidingAddressCreateIfNull(
            IResidingAddress existingCorrespondencePart,
            IResidingAddress incomingCorrespondencePart) {

        if (existingCorrespondencePart.getResidingAddress() != null &&
                incomingCorrespondencePart.getResidingAddress() != null) {
            updateAddress(existingCorrespondencePart.
                            getResidingAddress().getSimpleAddress(),
                    incomingCorrespondencePart.
                            getResidingAddress().getSimpleAddress());
        }
        // Create a new ResidingAddress object based on the incoming one
        else if (incomingCorrespondencePart.getResidingAddress() != null) {
            ResidingAddress residingAddress = new ResidingAddress();
            residingAddress.setSimpleAddress(new SimpleAddress());
            updateAddress(residingAddress.getSimpleAddress(),
                    incomingCorrespondencePart.getResidingAddress()
                            .getSimpleAddress());
            existingCorrespondencePart.setResidingAddress(residingAddress);
        }
        // Make sure the addressType field is set
        existingCorrespondencePart.getResidingAddress().
                getSimpleAddress().setAddressType(RESIDING_ADDRESS);
    }

    /**
     * Update PostalAddress if it exists. If none exists, create a
     * new PostalAddress and set the values.
     *
     * @param existingPostalAddress The existing
     *                              CorrespondencePart
     * @param incomingPostalAddress The incoming CorrespondencePart
     */
    public void updateCorrespondencePartPostalAddressCreateIfNull(
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
     * @param existingCorrespondencePart The existing CorrespondencePart
     * @param incomingCorrespondencePart The incoming CorrespondencePart
     */
    public void updateCorrespondencePartContactInformationCreateIfNull(
            IContactInformation existingCorrespondencePart,
            IContactInformation incomingCorrespondencePart) {

        if (existingCorrespondencePart.getContactInformation() != null &&
                incomingCorrespondencePart.getContactInformation() != null) {
            updateContactInformation(existingCorrespondencePart.
                            getContactInformation(),
                    incomingCorrespondencePart.
                            getContactInformation());
        }
        // Create a new ContactInformation object based on the incoming one
        else if (incomingCorrespondencePart.getContactInformation() != null) {
            existingCorrespondencePart.setContactInformation(
                    updateContactInformation(new ContactInformation(),
                            incomingCorrespondencePart.getContactInformation()));
        }
    }

    /**
     * Generate a Default CorrespondencePartUnit object that can be
     * associated with the identified RegistryEntry.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            final String registryEntrySystemId) {
        CorrespondencePartUnit suggestedCorrespondencePart =
                new CorrespondencePartUnit();

        findAndSetCorrespondencePartType(CORRESPONDENCE_PART_CODE_EA,
                suggestedCorrespondencePart);

        createTemplatePostalAddress(suggestedCorrespondencePart);
        createTemplateBusinessAddress(suggestedCorrespondencePart);
        createTemplateContactInformation(suggestedCorrespondencePart);
        suggestedCorrespondencePart.setContactPerson("Frank Grimes");

        CorrespondencePartUnitHateoas correspondencePartHateoas =
                new CorrespondencePartUnitHateoas(suggestedCorrespondencePart);
        correspondencePartHateoasHandler.addLinksOnTemplate(
                correspondencePartHateoas, new Authorisation());
        return correspondencePartHateoas;
    }

    /**
     * Generate a Default CorrespondencePartPerson object that can be
     * associated with the identified RegistryEntry.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to create a templated object for
     * @return the CorrespondencePartPerson object wrapped as a
     * CorrespondencePartPersonHateoas object
     */
    @Override
    public CorrespondencePartPersonHateoas
    generateDefaultCorrespondencePartPerson(final String registryEntrySystemId) {
        CorrespondencePartPerson suggestedCorrespondencePart =
                new CorrespondencePartPerson();

        findAndSetCorrespondencePartType(CORRESPONDENCE_PART_CODE_EA,
                suggestedCorrespondencePart);

        createTemplatePostalAddress(suggestedCorrespondencePart);
        createTemplateResidingAddress(suggestedCorrespondencePart);
        createTemplateContactInformation(suggestedCorrespondencePart);
        suggestedCorrespondencePart.setName("Frank Grimes");
        suggestedCorrespondencePart.setSocialSecurityNumber("01010012345");
        suggestedCorrespondencePart.setdNumber("01010012345");

        CorrespondencePartPersonHateoas correspondencePartHateoas =
                new CorrespondencePartPersonHateoas(suggestedCorrespondencePart);
        correspondencePartHateoasHandler.addLinksOnTemplate(
                correspondencePartHateoas, new Authorisation());
        return correspondencePartHateoas;
    }

    /**
     * Generate a Default CorrespondencePartInternal object that can be
     * associated with the identified RegistryEntry.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param registryEntrySystemId The systemId of the registryEntry object
     *                              you wish to create a templated object for
     * @return the CorrespondencePartInternal object wrapped as a
     * CorrespondencePartInternalHateoas object
     */
    @Override
    public CorrespondencePartInternalHateoas
    generateDefaultCorrespondencePartInternal(
            final String registryEntrySystemId) {
        CorrespondencePartInternal suggestedCorrespondencePart =
                new CorrespondencePartInternal();

        findAndSetCorrespondencePartType(CORRESPONDENCE_PART_CODE_EA,
                suggestedCorrespondencePart);

        CorrespondencePartInternalHateoas correspondencePartHateoas =
                new CorrespondencePartInternalHateoas(
                        suggestedCorrespondencePart);
        correspondencePartHateoasHandler.addLinksOnTemplate(
                correspondencePartHateoas, new Authorisation());
        return correspondencePartHateoas;
    }

    /**
     * Create a templated ContactInformation object
     *
     * @param correspondencePart templated correspondencePart
     */
    private void createTemplateContactInformation(final IContactInformation
                                                          correspondencePart) {
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setEmailAddress("nikita@example.com");
        contactInformation.setMobileTelephoneNumber("123456789");
        contactInformation.setTelephoneNumber("987654321");
        correspondencePart.setContactInformation(contactInformation);
    }

    /**
     * Create a templated address for the business address object
     *
     * @param correspondencePart templated correspondencePart
     */
    private void createTemplateBusinessAddress(IBusinessAddress
                                                       correspondencePart) {
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
        correspondencePart.setBusinessAddress(businessAddress);
    }


    /**
     * Create a templated address for the residing address object
     *
     * @param correspondencePart templated correspondencePart
     */
    private void createTemplateResidingAddress(final IResidingAddress
                                                       correspondencePart) {
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
        correspondencePart.setResidingAddress(residingAddress);
    }

    /**
     * Create a templated address for the postal address object
     *
     * @param correspondencePart templated correspondencePart
     */
    private void createTemplatePostalAddress(IPostalAddress
                                                     correspondencePart) {
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
        correspondencePart.setPostalAddress(postalAddress);
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
     * The incoming CorrespondencePartType does not have @id field set.
     * Therefore, we have to look it up in the database and make sure the
     * correct CorrespondencePartType is associated with the CorrespondencePart
     * <p>
     * If CorrespondencePartType type is not set, a
     * NikitaMalformedInputDataException is thrown. This means that this
     * method should originate based on a incoming request from a
     * Controller.
     *
     * @param correspondencePart Incoming correspondencePart
     */
    private void setCorrespondencePartType(
            @NotNull CorrespondencePart correspondencePart) {
        CorrespondencePartType incomingCorrespondencePartType =
                correspondencePart.getCorrespondencePartType();

        if (incomingCorrespondencePartType != null &&
                incomingCorrespondencePartType.getCode() != null) {
            CorrespondencePartType actualCorrespondencePartType =
                    correspondencePartTypeRepository.findByCode(
                            incomingCorrespondencePartType.getCode());
            if (actualCorrespondencePartType != null) {
                correspondencePart.setCorrespondencePartType(
                        actualCorrespondencePartType);
            }
        } else {
            throw new NikitaMalformedInputDataException("Missing required " +
                    "CorrespondencePartType value");
        }
    }

    /**
     * Internal helper method to retrieve a correspondencePartType object
     * given a code e.g. EA returns EA, Avsender. Note if this does not find a
     * code it throws an exception. So this should only be called as part of
     * a process where that type of action is acceptable.
     *
     * @param code               A code identifying a correspondencePartType
     * @param correspondencePart The correct correspondencePartType
     */
    private void findAndSetCorrespondencePartType(String code,
                                                  CorrespondencePart correspondencePart) {
        CorrespondencePartType correspondencePartType =
                correspondencePartTypeService.
                        findByCode(code);
        if (correspondencePartType == null) {
            throw new NikitaException("Internal error, metadata missing. [" +
                    CORRESPONDENCE_PART_CODE_EA + "] returns no value");
        }
        correspondencePart.setCorrespondencePartType(correspondencePartType);
    }
}
