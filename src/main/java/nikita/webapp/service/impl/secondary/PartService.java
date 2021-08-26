package nikita.webapp.service.impl.secondary;

import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.casehandling.secondary.*;
import nikita.common.model.noark5.v5.hateoas.secondary.PartHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartPersonHateoas;
import nikita.common.model.noark5.v5.hateoas.secondary.PartUnitHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.*;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.model.noark5.v5.metadata.PartRole;
import nikita.common.model.noark5.v5.secondary.Part;
import nikita.common.model.noark5.v5.secondary.PartPerson;
import nikita.common.model.noark5.v5.secondary.PartUnit;
import nikita.common.repository.n5v5.secondary.IPartRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.IPartHateoasHandler;
import nikita.webapp.service.application.IPatchService;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.IBSMService;
import nikita.webapp.service.interfaces.metadata.IMetadataService;
import nikita.webapp.service.interfaces.odata.IODataService;
import nikita.webapp.service.interfaces.secondary.IPartService;
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
import static nikita.common.config.Constants.TEMPLATE_PART_ROLE_CODE;
import static nikita.common.config.N5ResourceMappings.*;

@Service
public class PartService
        extends NoarkService
        implements IPartService {

    private static final Logger logger =
            LoggerFactory.getLogger(PartService.class);

    private final IPartRepository partRepository;
    private final IMetadataService metadataService;
    private final IPartHateoasHandler partHateoasHandler;
    private final IBSMService bsmService;

    public PartService(EntityManager entityManager,
                       ApplicationEventPublisher applicationEventPublisher,
                       IODataService odataService,
                       IPatchService patchService,
                       IPartRepository partRepository,
                       IMetadataService metadataService,
                       IPartHateoasHandler partHateoasHandler,
                       IBSMService bsmService) {
        super(entityManager, applicationEventPublisher, patchService, odataService);
        this.partRepository = partRepository;
        this.metadataService = metadataService;
        this.partHateoasHandler = partHateoasHandler;
        this.bsmService = bsmService;
    }

    // All CREATE methods

    @Override
    @Transactional
    public PartPersonHateoas createNewPartPerson(
            @NotNull final PartPerson part, @NotNull final RecordEntity record) {
        validatePartRole(part);
        createPerson(part);
        record.addPart(part);
        return packAsPartPersonHateoas(partRepository.save(part));
    }

    @Override
    @Transactional
    public PartPersonHateoas createNewPartPerson(
            @NotNull final PartPerson part, @NotNull final File file) {
        validatePartRole(part);
        createPerson(part);
        file.addPart(part);
        return packAsPartPersonHateoas(partRepository.save(part));
    }

    @Override
    @Transactional
    public PartUnitHateoas createNewPartUnit(
            @NotNull final PartUnit part, @NotNull final RecordEntity record) {
        validatePartRole(part);
        createUnit(part);
        record.addPart(part);
        return packAsPartUnitHateoas(partRepository.save(part));
    }

    @Override
    @Transactional
    public PartUnitHateoas createNewPartUnit(
            @NotNull final PartUnit part, @NotNull final File file) {
        validatePartRole(part);
        createUnit(part);
        file.addPart(part);
        return packAsPartUnitHateoas(partRepository.save(part));
    }

    @Override
    @Transactional
    public PartUnitHateoas createNewPartUnit(
            @NotNull final PartUnit partUnit,
            @NotNull final DocumentDescription documentDescription) {
        validatePartRole(partUnit);
        createUnit(partUnit);
        documentDescription.addPart(partUnit);
        return packAsPartUnitHateoas(partRepository.save(partUnit));
    }

    @Override
    @Transactional
    public PartPersonHateoas createNewPartPerson(
            @NotNull final PartPerson partPerson,
            @NotNull final DocumentDescription documentDescription) {
        validatePartRole(partPerson);
        createPerson(partPerson);
        partPerson.addDocumentDescription(documentDescription);
        return packAsPartPersonHateoas(partRepository.save(partPerson));
    }

    // All READ methods

    @Override
    public Part findBySystemId(@NotNull final UUID systemId) {
        return getPartOrThrow(systemId);
    }

    @Override
    public PartPersonHateoas findPartPersonBySystemId(
            @NotNull final UUID systemId) {
        return packAsPartPersonHateoas((PartPerson) getPartOrThrow(systemId));
    }

    @Override
    public PartUnitHateoas findPartUnitBySystemId(
            @NotNull final UUID systemId) {
        return packAsPartUnitHateoas((PartUnit) getPartOrThrow(systemId));
    }

    @Override
    protected Optional<BSMMetadata> findBSMByName(@NotNull final String name) {
        return bsmService.findBSMByName(name);
    }

    // All UPDATE methods

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
    @Transactional
    public PartPersonHateoas updatePartPerson(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final PartPerson incomingPart) {
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
        return packAsPartPersonHateoas(existingPart);
    }

    @Override
    @Transactional
    public PartUnitHateoas updatePartUnit(
            @NotNull final UUID systemId, @NotNull final Long version,
            @NotNull final PartUnit incomingPart) {

        PartUnit existingPart = (PartUnit) getPartOrThrow(systemId);

        // Copy all the values you are allowed to copy ....
        // First the values
        existingPart.setName(
                incomingPart.getName());
        existingPart.setUnitIdentifier(
                incomingPart.getUnitIdentifier());
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
        return packAsPartUnitHateoas(existingPart);
    }

    @Override
    @Transactional
    public PartHateoas handleUpdate(
            @NotNull final UUID systemId,
            @NotNull final PatchObjects patchObjects) {
        return packAsHateoas((Part) handlePatch(systemId, patchObjects));
    }

    @Override
    @Transactional
    public Object associateBSM(@NotNull final UUID systemId,
                               @NotNull final List<BSMBase> bsm) {
        Part part = getPartOrThrow(systemId);
        part.addReferenceBSMBase(bsm);
        return part;
    }

    // All DELETE methods
    @Override
    @Transactional
    public void deletePartPerson(@NotNull final UUID systemId) {
        PartPerson partPerson = (PartPerson) getPartOrThrow(systemId);
        for (RecordEntity record : partPerson.getReferenceRecord()) {
            record.removePart(partPerson);
        }
        for (File file : partPerson.getReferenceFile()) {
            file.removePart(partPerson);
        }
        for (BSMBase bsmBase : partPerson.getReferenceBSMBase()) {
            bsmBase.setReferencePart(null);
            partPerson.getReferenceBSMBase().remove(bsmBase);
        }
        partRepository.delete(partPerson);
    }

    @Override
    @Transactional
    public void deletePartUnit(@NotNull final UUID systemId) {
        PartUnit partUnit = (PartUnit) getPartOrThrow(systemId);
        for (RecordEntity record : partUnit.getReferenceRecord()) {
            record.removePart(partUnit);
        }
        for (File file : partUnit.getReferenceFile()) {
            file.removePart(partUnit);
        }
        for (BSMBase bsmBase : partUnit.getReferenceBSMBase()) {
            bsmBase.setReferencePart(null);
            partUnit.getReferenceBSMBase().remove(bsmBase);
        }
        for (DocumentDescription documentDescription :
                partUnit.getReferenceDocumentDescription()) {
            documentDescription.removePart(partUnit);
        }
        partRepository.delete(partUnit);
    }

    // Internal template methods

    /**
     * Generate a Default PartUnit object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                 you wish to create a templated object for
     * @return the PartUnit object wrapped as a
     * PartUnitHateoas object
     */
    @Override
    public PartUnitHateoas generateDefaultPartUnit(
            @NotNull final UUID systemId) {
        PartUnit suggestedPart = new PartUnit();
        suggestedPart.setVersion(-1L, true);
        setDefaultPartRole(suggestedPart);
        return packAsPartUnitHateoas(suggestedPart);
    }

    /**
     * Generate a Default PartPerson object that can be
     * associated with the identified Record.
     * <p>
     * Note. Ideally this method would be configurable based on the logged in
     * user and the business area they are working with. A generic Noark core
     * like this does not have scope for that kind of functionality.
     *
     * @param systemId The systemId of the record object
     *                       you wish to create a templated object for
     * @return the PartPerson object wrapped as a
     * PartPersonHateoas object
     */
    @Override
    public PartPersonHateoas generateDefaultPartPerson(
            @NotNull final UUID systemId) {
        PartPerson suggestedPart = new PartPerson();
        suggestedPart.setVersion(-1L, true);
        setDefaultPartRole(suggestedPart);
        return packAsPartPersonHateoas(suggestedPart);
    }

    // Internal helper methods

    private void createPerson(@NotNull final PartPerson part) {
        ContactInformation contactInformation = part.getContactInformation();

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
     * @param partSystemId systemId of part to retrieve
     * @return the retrieved Part
     */
    private Part getPartOrThrow(@NotNull final UUID partSystemId) {
        Part part = partRepository.findBySystemId(partSystemId);
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
    private void updatePartUnitBusinessAddressCreateIfNull(
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
    private void updatePartResidingAddressCreateIfNull(
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
     * @param existingPart The existing Part
     * @param incomingPart The incoming Part
     */
    private void updatePartPostalAddressCreateIfNull(
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
    private void updatePartContactInformationCreateIfNull(
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

    private void setDefaultPartRole(@NotNull final Part part) {
        PartRole partRole = (PartRole) metadataService
                .findValidMetadataByEntityTypeOrThrow(PART_ROLE,
                        TEMPLATE_PART_ROLE_CODE, null);
        part.setPartRole(partRole);
    }

    private void validatePartRole(Part part) {
        // Assume value already set, as the deserialiser will enforce it.
        PartRole partRole = (PartRole) metadataService.findValidMetadata(
                part.getPartRole());
        part.setPartRole(partRole);
    }

    public PartHateoas packAsHateoas(@NotNull final Part part) {
        PartHateoas partHateoas = new PartHateoas(part);
        applyLinksAndHeader(partHateoas, partHateoasHandler);
        return partHateoas;
    }

    public PartPersonHateoas packAsPartPersonHateoas(
            @NotNull final PartPerson partPerson) {
        PartPersonHateoas partPersonHateoas = new PartPersonHateoas(partPerson);
        applyLinksAndHeader(partPersonHateoas, partHateoasHandler);
        return partPersonHateoas;
    }

    public PartUnitHateoas packAsPartUnitHateoas(@NotNull final PartUnit partUnit) {
        PartUnitHateoas partUnitHateoas = new PartUnitHateoas(partUnit);
        applyLinksAndHeader(partUnitHateoas, partHateoasHandler);
        return partUnitHateoas;
    }
}
