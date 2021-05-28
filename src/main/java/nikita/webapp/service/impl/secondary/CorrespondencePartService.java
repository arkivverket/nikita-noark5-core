package nikita.webapp.service.impl.secondary;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartInternalHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.*;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;
import nikita.common.repository.n5v5.secondary.ICorrespondencePartRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.IBSMService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
import static nikita.common.config.N5ResourceMappings.*;

@Service
public class CorrespondencePartService
        extends NoarkService
        implements ICorrespondencePartService {

    private static final Logger logger =
            LoggerFactory.getLogger(CorrespondencePartService.class);

    private final ICorrespondencePartRepository correspondencePartRepository;
    private final ICorrespondencePartHateoasHandler
            correspondencePartHateoasHandler;
    private final IMetadataService metadataService;
    private final IBSMService bsmService;

    public CorrespondencePartService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IODataService odataService,
            IPatchService patchService,
            ICorrespondencePartRepository correspondencePartRepository,
            ICorrespondencePartHateoasHandler correspondencePartHateoasHandler,
            IMetadataService metadataService, IBSMService bsmService) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.correspondencePartRepository = correspondencePartRepository;
        this.correspondencePartHateoasHandler = correspondencePartHateoasHandler;
        this.metadataService = metadataService;
        this.bsmService = bsmService;
    }

    // All CREATE methods

    @Override
    @Transactional
    public CorrespondencePartPersonHateoas
    createNewCorrespondencePartPerson(
            @NotNull final CorrespondencePartPerson correspondencePart,
            @NotNull final Record record) {

        validateCorrespondencePartType(correspondencePart);
        ContactInformation contactInformation
                = correspondencePart.getContactInformation();
        if (contactInformation != null) {
            contactInformation.setCorrespondencePartPerson(correspondencePart);
        }
        PostalAddress postalAddress = correspondencePart.getPostalAddress();
        if (null != postalAddress) {
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
            postalAddress.setReferenceCorrespondencePartPerson(
                    correspondencePart);
        }
        ResidingAddress residingAddress =
                correspondencePart.getResidingAddress();
        if (null != residingAddress) {
            residingAddress.getSimpleAddress().setAddressType(RESIDING_ADDRESS);
            residingAddress.setCorrespondencePartPerson(correspondencePart);
        }
        record.addCorrespondencePart(correspondencePart);
        return packAsCorrespondencePartPersonHateoas(
                correspondencePartRepository.save(correspondencePart));
    }

    /**
     * The correspondencePart.setPostalAddress(postalAddress); should not be
     * necessary as the deserialiser will already have done this. Check this
     * to see if we really need to do this.
     *
     * @param correspondencePart Incoming correspondencePartUni
     * @param record             existing record retrieved from database
     * @return correspondencePart wraped as a correspondencePartUnitHateaos
     */
    @Override
    @Transactional
    public CorrespondencePartUnitHateoas createNewCorrespondencePartUnit(
            @NotNull final CorrespondencePartUnit correspondencePart,
            @NotNull final Record record) {
        validateCorrespondencePartType(correspondencePart);
        // Set NikitaEntity values for ContactInformation, PostalAddress,
        // BusinessAddress
        PostalAddress postalAddress = correspondencePart.getPostalAddress();
        if (null != postalAddress) {
            postalAddress.getSimpleAddress().setAddressType(POSTAL_ADDRESS);
            correspondencePart.setPostalAddress(postalAddress);
        }
        BusinessAddress businessAddress =
                correspondencePart.getBusinessAddress();
        if (null != businessAddress) {
            businessAddress.getSimpleAddress().setAddressType(BUSINESS_ADDRESS);
            correspondencePart.setBusinessAddress(businessAddress);
        }
        record.addCorrespondencePart(correspondencePart);
        return packAsCorrespondencePartUnitHateoas(
                correspondencePartRepository.save(correspondencePart));
    }

    @Override
    @Transactional
    public CorrespondencePartInternalHateoas
    createNewCorrespondencePartInternal(
            @NotNull final CorrespondencePartInternal correspondencePart,
            @NotNull final Record record) {
        validateCorrespondencePartType(correspondencePart);
        record.addCorrespondencePart(correspondencePart);
        return packAsCorrespondencePartInternalHateoas(
                correspondencePartRepository.save(correspondencePart));
    }

    // All READ methods

    @Override
    public CorrespondencePart findBySystemId(@NotNull final UUID systemId) {
        return getCorrespondencePartOrThrow(systemId);
    }

    @Override
    public CorrespondencePartInternalHateoas
    findCorrespondencePartInternalBySystemId(UUID systemId) {
        return packAsCorrespondencePartInternalHateoas((CorrespondencePartInternal)
                getCorrespondencePartOrThrow(systemId));
    }

    @Override
    public CorrespondencePartPersonHateoas
    findCorrespondencePartPersonBySystemId(UUID systemId) {
        return packAsCorrespondencePartPersonHateoas((CorrespondencePartPerson)
                getCorrespondencePartOrThrow(systemId));
    }

    @Override
    public CorrespondencePartUnitHateoas
    findCorrespondencePartUnitBySystemId(UUID systemId) {
        return packAsCorrespondencePartUnitHateoas((CorrespondencePartUnit)
                getCorrespondencePartOrThrow(systemId));
    }

    // All UPDATE methods

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
    @Transactional
    public CorrespondencePartPersonHateoas
    updateCorrespondencePartPerson(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CorrespondencePartPerson incomingCorrespondencePart) {
        CorrespondencePartPerson existingCorrespondencePart =
                (CorrespondencePartPerson)
                        getCorrespondencePartOrThrow(systemId);

        updateCorrespondencePartType(incomingCorrespondencePart,
                existingCorrespondencePart);

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
        return packAsCorrespondencePartPersonHateoas(
                existingCorrespondencePart);
    }

    private void updateCorrespondencePartType(
            @NotNull final CorrespondencePart incomingCorrespondencePart,
            @NotNull final CorrespondencePart existingCorrespondencePart) {
        // Only copy if changed, in case it has an historical value
        if (existingCorrespondencePart.getCorrespondencePartType()
                != incomingCorrespondencePart.getCorrespondencePartType()) {
            validateCorrespondencePartType(incomingCorrespondencePart);
            existingCorrespondencePart.setCorrespondencePartType
                    (incomingCorrespondencePart.getCorrespondencePartType());
        }
    }

    @Override
    @Transactional
    public CorrespondencePartInternalHateoas updateCorrespondencePartInternal(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CorrespondencePartInternal incomingCorrespondencePart) {
        CorrespondencePartInternal existingCorrespondencePart =
                (CorrespondencePartInternal)
                        getCorrespondencePartOrThrow(systemId);
        // Copy all the values you are allowed to copy ....

        updateCorrespondencePartType(incomingCorrespondencePart,
                existingCorrespondencePart);

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
        return packAsCorrespondencePartInternalHateoas(
                existingCorrespondencePart);
    }

    @Override
    @Transactional
    public CorrespondencePartUnitHateoas updateCorrespondencePartUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final CorrespondencePartUnit incomingCorrespondencePart) {

        CorrespondencePartUnit existingCorrespondencePart =
                (CorrespondencePartUnit) getCorrespondencePartOrThrow(systemId);

        updateCorrespondencePartType(incomingCorrespondencePart,
                existingCorrespondencePart);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingCorrespondencePart.setName(
                incomingCorrespondencePart.getName());
        existingCorrespondencePart.setUnitIdentifier(
                incomingCorrespondencePart.getUnitIdentifier());
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
        return packAsCorrespondencePartUnitHateoas(existingCorrespondencePart);
    }

    @Override
    @Transactional
    public CorrespondencePartHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects) {
        return packAsHateoas((CorrespondencePart)
                handlePatch(systemId, patchObjects));
    }

    // All template methods

    private void createTemplateCorrespondencePartType(
            @NotNull final CorrespondencePart correspondencePart) {
        CorrespondencePartType correspondencePartType = (CorrespondencePartType)
                metadataService.findValidMetadataByEntityTypeOrThrow
                        (CORRESPONDENCE_PART_TYPE, CORRESPONDENCE_PART_CODE_EA, null);
        correspondencePart.setCorrespondencePartType(correspondencePartType);

    }

    // All UPDATE methods

    /**
     * Delete a CorrespondencePartUnit identified by the given systemId
     * <p>
     * Note. This assumes all children have also been deleted.
     *
     * @param systemId The systemId of the CorrespondencePart object you wish
     *                 to delete
     */
    @Override
    @Transactional
    public void deleteCorrespondencePartUnit(@NotNull final UUID systemId) {
        CorrespondencePartUnit correspondencePartUnit = (CorrespondencePartUnit)
                getCorrespondencePartOrThrow(systemId);
        Record record = correspondencePartUnit.getReferenceRecord();
        record.removeCorrespondencePart(correspondencePartUnit);
        for (BSMBase bsmBase : correspondencePartUnit.getReferenceBSMBase()) {
            bsmBase.setReferenceCorrespondencePart(null);
        }
        correspondencePartRepository.delete(correspondencePartUnit);
    }

    /**
     * Delete a CorrespondencePartPerson identified by the given systemId
     * <p>
     * Note. This assumes all children have also been deleted.
     *
     * @param systemId The systemId of the CorrespondencePart object you wish
     *                 to delete
     */
    @Override
    @Transactional
    public void deleteCorrespondencePartPerson(@NotNull final UUID systemId) {
        CorrespondencePartPerson correspondencePartPerson = (CorrespondencePartPerson)
                getCorrespondencePartOrThrow(systemId);
        Record record = correspondencePartPerson.getReferenceRecord();
        record.removeCorrespondencePart(correspondencePartPerson);
        for (BSMBase bsmBase : correspondencePartPerson.getReferenceBSMBase()) {
            bsmBase.setReferenceCorrespondencePart(null);
        }
        correspondencePartRepository.delete(correspondencePartPerson);
    }

    /**
     * Delete a CorrespondencePartInternal identified by the given systemId
     * <p>
     * Note. This assumes all children have also been deleted.
     *
     * @param systemId The systemId of the CorrespondencePart object you wish
     *                 to delete
     */
    @Override
    @Transactional
    public void deleteCorrespondencePartInternal(@NotNull final UUID systemId) {
        correspondencePartRepository.delete(
                getCorrespondencePartOrThrow(systemId));
    }

    /**
     * Delete all objects belonging to the user identified by ownedBy
     */
    @Override
    @Transactional
    public void deleteAllByOwnedBy() {
        correspondencePartRepository.deleteByOwnedBy(getUser());
    }

    // Internal helper methods

    /**
     * Update BusinessAddress if it exists. If none exists, create a
     * new BusinessAddress and set the values.
     *
     * @param existingCorrespondencePart The existing CorrespondencePart
     * @param incomingCorrespondencePart The incoming CorrespondencePart
     */
    private void updateCorrespondencePartUnitBusinessAddressCreateIfNull(
            @NotNull final IBusinessAddress existingCorrespondencePart,
            @NotNull final IBusinessAddress incomingCorrespondencePart) {

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
        // Make sure the addressType field is set
        if (existingCorrespondencePart.getBusinessAddress() != null &&
                existingCorrespondencePart.getBusinessAddress().
                        getSimpleAddress() != null) {
            existingCorrespondencePart.getBusinessAddress().getSimpleAddress().
                    setAddressType(BUSINESS_ADDRESS);
        }
    }

    /**
     * Update ResidingAddress if it exists. If none exists, create a
     * new ResidingAddress and set the values.
     *
     * @param existingCorrespondencePart The existing CorrespondencePart
     * @param incomingCorrespondencePart The incoming CorrespondencePart
     */
    private void updateCorrespondencePartResidingAddressCreateIfNull(
            @NotNull final IResidingAddress existingCorrespondencePart,
            @NotNull final IResidingAddress incomingCorrespondencePart) {

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
        if (existingCorrespondencePart.getResidingAddress() != null &&
                existingCorrespondencePart.getResidingAddress().
                        getSimpleAddress() != null) {
            existingCorrespondencePart.getResidingAddress().getSimpleAddress().
                    setAddressType(RESIDING_ADDRESS);
        }
    }

    /**
     * Update PostalAddress if it exists. If none exists, create a
     * new PostalAddress and set the values.
     *
     * @param existingPostalAddress The existing
     *                              CorrespondencePart
     * @param incomingPostalAddress The incoming CorrespondencePart
     */
    private void updateCorrespondencePartPostalAddressCreateIfNull(
            @NotNull final IPostalAddress existingPostalAddress,
            @NotNull final IPostalAddress incomingPostalAddress) {

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
        if (existingPostalAddress.getPostalAddress() != null &&
                existingPostalAddress.getPostalAddress().
                        getSimpleAddress() != null) {
            existingPostalAddress.getPostalAddress().getSimpleAddress().
                    setAddressType(POSTAL_ADDRESS);
        }
    }

    /**
     * Update ContactInformation if it exists. If none exists, create a
     * new ContactInformation and set the values.
     *
     * @param existingCorrespondencePart The existing CorrespondencePart
     * @param incomingCorrespondencePart The incoming CorrespondencePart
     */
    private void updateCorrespondencePartContactInformationCreateIfNull(
            @NotNull final IContactInformation existingCorrespondencePart,
            @NotNull final IContactInformation incomingCorrespondencePart) {

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
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the CorrespondencePartUnit object wrapped as a
     * CorrespondencePartUnitHateoas object
     */
    @Override
    public CorrespondencePartUnitHateoas generateDefaultCorrespondencePartUnit(
            @NotNull final UUID recordSystemId) {
        CorrespondencePartUnit suggestedCorrespondencePart =
                new CorrespondencePartUnit();
        suggestedCorrespondencePart.setVersion(-1L, true);
        createTemplateCorrespondencePartType(suggestedCorrespondencePart);
        return packAsCorrespondencePartUnitHateoas(suggestedCorrespondencePart);
    }

    /**
     * Generate a Default CorrespondencePartPerson object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the CorrespondencePartPerson object wrapped as a
     * CorrespondencePartPersonHateoas object
     */
    @Override
    public CorrespondencePartPersonHateoas
    generateDefaultCorrespondencePartPerson(
            @NotNull final UUID recordSystemId) {
        CorrespondencePartPerson suggestedCorrespondencePart =
                new CorrespondencePartPerson();
        suggestedCorrespondencePart.setVersion(-1L, true);
        createTemplateCorrespondencePartType(suggestedCorrespondencePart);
        return packAsCorrespondencePartPersonHateoas(suggestedCorrespondencePart);
    }

    /**
     * Generate a Default CorrespondencePartInternal object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param recordSystemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the CorrespondencePartInternal object wrapped as a
     * CorrespondencePartInternalHateoas object
     */
    @Override
    public CorrespondencePartInternalHateoas
    generateDefaultCorrespondencePartInternal(
            @NotNull final UUID recordSystemId) {
        CorrespondencePartInternal suggestedCorrespondencePart =
                new CorrespondencePartInternal();

        createTemplateCorrespondencePartType(suggestedCorrespondencePart);
        suggestedCorrespondencePart.setVersion(-1L, true);
        return packAsCorrespondencePartInternalHateoas(
                suggestedCorrespondencePart);
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
    private ContactInformation updateContactInformation(
            @NotNull final IContactInformationEntity existingContactInformation,
            @NotNull final IContactInformationEntity incomingContactInformation) {

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
    private void updateAddress(@NotNull final SimpleAddress existingAddress,
                               @NotNull final SimpleAddress incomingAddress) {

        existingAddress.setAddressType(incomingAddress.getAddressType());
        existingAddress.setAddressLine1(incomingAddress.getAddressLine1());
        existingAddress.setAddressLine2(incomingAddress.getAddressLine2());
        existingAddress.setAddressLine3(incomingAddress.getAddressLine3());
        existingAddress.setPostalNumber(incomingAddress.getPostalNumber());
        existingAddress.setPostalTown(incomingAddress.getPostalTown());
        existingAddress.setCountryCode(incomingAddress.getCountryCode());
    }

    private void validateCorrespondencePartType(
            @NotNull final CorrespondencePart correspondencePart) {
        // Assume value already set, as the deserialiser will enforce it.
        CorrespondencePartType correspondencePartType =
                (CorrespondencePartType) metadataService.findValidMetadata(
                        correspondencePart.getCorrespondencePartType());
        correspondencePart.setCorrespondencePartType
                (correspondencePartType);
    }

    @Override
    protected Optional<BSMMetadata> findBSMByName(@NotNull final String name) {
        return bsmService.findBSMByName(name);
    }

    @Override
    @Transactional
    public Object associateBSM(@NotNull final UUID systemId,
                               @NotNull final List<BSMBase> bsm) {
        CorrespondencePart correspondencePart =
                getCorrespondencePartOrThrow(systemId);
        correspondencePart.addReferenceBSMBase(bsm);
        return correspondencePart;
    }

    public CorrespondencePartHateoas packAsHateoas(
            @NotNull final CorrespondencePart correspondencePart) {
        CorrespondencePartHateoas correspondencePartHateoas =
                new CorrespondencePartHateoas(correspondencePart);
        applyLinksAndHeader(correspondencePartHateoas,
                correspondencePartHateoasHandler);
        return correspondencePartHateoas;
    }

    public CorrespondencePartPersonHateoas
    packAsCorrespondencePartPersonHateoas(
            @NotNull final CorrespondencePartPerson correspondencePartPerson) {
        CorrespondencePartPersonHateoas correspondencePartPersonHateoas =
                new CorrespondencePartPersonHateoas(correspondencePartPerson);
        applyLinksAndHeader(correspondencePartPersonHateoas,
                correspondencePartHateoasHandler);
        return correspondencePartPersonHateoas;
    }

    public CorrespondencePartInternalHateoas
    packAsCorrespondencePartInternalHateoas(
            @NotNull final CorrespondencePartInternal correspondencePartInternal) {
        CorrespondencePartInternalHateoas correspondencePartInternalHateoas =
                new CorrespondencePartInternalHateoas(correspondencePartInternal);
        applyLinksAndHeader(correspondencePartInternalHateoas,
                correspondencePartHateoasHandler);
        return correspondencePartInternalHateoas;
    }

    public CorrespondencePartUnitHateoas
    packAsCorrespondencePartUnitHateoas(
            @NotNull final CorrespondencePartUnit correspondencePartUnit) {
        CorrespondencePartUnitHateoas correspondencePartUnitHateoas =
                new CorrespondencePartUnitHateoas(correspondencePartUnit);
        applyLinksAndHeader(correspondencePartUnitHateoas,
                correspondencePartHateoasHandler);
        return correspondencePartUnitHateoas;
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid CorrespondencePart back. If there
     * is no valid CorrespondencePart, an exception is thrown
     *
     * @param systemId systemId of correspondencePart to
     *                 retrieve
     * @return the retrieved CorrespondencePart
     */
    private CorrespondencePart getCorrespondencePartOrThrow(
            @NotNull final UUID systemId) {
        CorrespondencePart correspondencePart =
                correspondencePartRepository
                        .findBySystemId(systemId);
        if (correspondencePart == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " CorrespondencePart, " +
                    "using systemId " + systemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return correspondencePart;
    }
}
