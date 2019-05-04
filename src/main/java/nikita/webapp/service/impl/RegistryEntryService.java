package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.File;
import nikita.common.model.noark5.v4.casehandling.Precedence;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.casehandling.secondary.*;
import nikita.common.model.noark5.v4.hateoas.casehandling.CorrespondencePartUnitHateoas;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.IBusinessAddress;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.IContactInformation;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.IPostalAddress;
import nikita.common.model.noark5.v4.interfaces.entities.casehandling.IResidingAddress;
import nikita.common.model.noark5.v4.metadata.CorrespondencePartType;
import nikita.common.repository.n5v4.IRegistryEntryRepository;
import nikita.common.repository.n5v4.metadata.ICorrespondencePartTypeRepository;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.secondary.ICorrespondencePartHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.metadata.ICorrespondencePartTypeService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.common.config.MetadataConstants.CORRESPONDENCE_PART_CODE_EA;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

@Service
@Transactional
public class RegistryEntryService
        implements IRegistryEntryService {

    private static final Logger logger =
            LoggerFactory.getLogger(RegistryEntryService.class);
    private ICorrespondencePartService correspondencePartService;
    private IPrecedenceService precedenceService;
    private IRegistryEntryRepository registryEntryRepository;
    private ICorrespondencePartTypeRepository correspondencePartTypeRepository;
    private ApplicationEventPublisher applicationEventPublisher;
    private EntityManager entityManager;
    private ICorrespondencePartHateoasHandler correspondencePartHateoasHandler;
    private ICorrespondencePartTypeService correspondencePartTypeService;

    public RegistryEntryService(
            ICorrespondencePartService correspondencePartService,
            IPrecedenceService precedenceService,
            IRegistryEntryRepository registryEntryRepository,
            ICorrespondencePartTypeRepository correspondencePartTypeRepository,
            ApplicationEventPublisher applicationEventPublisher,
            EntityManager entityManager,
            ICorrespondencePartHateoasHandler correspondencePartHateoasHandler,
            ICorrespondencePartTypeService correspondencePartTypeService) {
        this.correspondencePartService = correspondencePartService;
        this.precedenceService = precedenceService;
        this.registryEntryRepository = registryEntryRepository;
        this.correspondencePartTypeRepository = correspondencePartTypeRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.entityManager = entityManager;
        this.correspondencePartHateoasHandler = correspondencePartHateoasHandler;
        this.correspondencePartTypeService = correspondencePartTypeService;
    }

    @Override
    public RegistryEntry save(@NotNull RegistryEntry registryEntry) {
        setNikitaEntityValues(registryEntry);
        setSystemIdEntityValues(registryEntry);
        setCreateEntityValues(registryEntry);
        checkDocumentMediumValid(registryEntry);
        registryEntry.setRecordDate(new Date());
        File file = registryEntry.getReferenceFile();
        if (null != file) {
            Long numberAssociated =
                    registryEntryRepository.countByReferenceFile(file) + 1;
            registryEntry.setRegistryEntryNumber(numberAssociated.intValue());
            registryEntry.setRecordId(file.getFileId() + "-" + numberAssociated);
        }
        registryEntryRepository.save(registryEntry);
        return registryEntry;
    }

    @Override
    public List<CorrespondencePartPerson>
    getCorrespondencePartPersonAssociatedWithRegistryEntry(String systemID) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        return registryEntry.getReferenceCorrespondencePartPerson();
    }

    /*
        @Override
        public List<CorrespondencePartInternal> getCorrespondencePartInternalAssociatedWithRegistryEntry(String systemID) {
            RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
            return registryEntry.getReferenceCorrespondencePartInternal();
        }
    */
    @Override
    public List<CorrespondencePartUnit>
    getCorrespondencePartUnitAssociatedWithRegistryEntry(String systemID) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        return registryEntry.getReferenceCorrespondencePartUnit();
    }

    @Override
    public CorrespondencePartPerson
    createCorrespondencePartPersonAssociatedWithRegistryEntry(
            @NotNull String systemID,
            @NotNull CorrespondencePartPerson correspondencePart) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);

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

        // bidirectional relationship @ManyToMany, set both sides of relationship
        registryEntry.getReferenceCorrespondencePartPerson().
                add(correspondencePart);
        correspondencePart.addRegistryEntry(registryEntry);
        return correspondencePartService.
                createNewCorrespondencePartPerson(correspondencePart);
    }

    /*
  TODO: Temp disabled!
    @Override
    public CorrespondencePartInternal createCorrespondencePartInternalAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartInternal correspondencePart) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);

        setNikitaEntityValues(correspondencePart);
        setSystemIdEntityValues(correspondencePart);
        // bidirectional relationship @ManyToMany, set both sides of relationship
        registryEntry.getReferenceCorrespondencePartInternal().add(correspondencePart);
        correspondencePart.getReferenceRegistryEntry().add(registryEntry);
        return correspondencePartService.createNewCorrespondencePartInternal(correspondencePart);
    }
*/
    @Override
    public CorrespondencePartUnitHateoas
    createCorrespondencePartUnitAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartUnit correspondencePart) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);

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

        CorrespondencePartUnitHateoas correspondencePartUnitHateoas =
                new CorrespondencePartUnitHateoas(correspondencePartService.
                        createNewCorrespondencePartUnit(correspondencePart));
        correspondencePartHateoasHandler.addLinks(correspondencePartUnitHateoas,
                new Authorisation());
        applicationEventPublisher.publishEvent(
                new AfterNoarkEntityCreatedEvent(this,
                        correspondencePart));

        return correspondencePartUnitHateoas;
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
            String registryEntrySystemId) {
        CorrespondencePartUnit suggestedCorrespondencePart =
                new CorrespondencePartUnit();

        CorrespondencePartType correspondencePartType =
                correspondencePartTypeService.
                        findByCode(CORRESPONDENCE_PART_CODE_EA);
        if (correspondencePartType == null) {
            throw new NikitaException("Internal error, metadata missing. [" +
                    CORRESPONDENCE_PART_CODE_EA + "] returns no value");
        }

        suggestedCorrespondencePart.
                setCorrespondencePartType(correspondencePartType);

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

    // All READ operations
    public List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip) {

        int maxPageSize = 10;
        if (top == null || top > maxPageSize) {
            top = maxPageSize;
        }
        if (skip == null) {
            skip = 0;
        }

        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RegistryEntry> criteriaQuery = criteriaBuilder.createQuery(RegistryEntry.class);
        Root<RegistryEntry> from = criteriaQuery.from(RegistryEntry.class);
        CriteriaQuery<RegistryEntry> select = criteriaQuery.select(from);

        criteriaQuery.where(criteriaBuilder.equal(from.get("ownedBy"), loggedInUser));
        TypedQuery<RegistryEntry> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(skip);
        typedQuery.setMaxResults(top);
        return typedQuery.getResultList();
    }

    // systemId
    public RegistryEntry findBySystemId(String systemId) {
        return getRegistryEntryOrThrow(systemId);
    }


    @Override
    public Precedence createPrecedenceAssociatedWithRecord(String registryEntrySystemID, Precedence precedence) {

        RegistryEntry registryEntry = getRegistryEntryOrThrow(registryEntrySystemID);
        setNikitaEntityValues(precedence);
        setSystemIdEntityValues(precedence);
        // bidirectional relationship @ManyToMany, set both sides of relationship
        registryEntry.getReferencePrecedence().add(precedence);
        precedence.getReferenceRegistryEntry().add(registryEntry);
        return precedenceService.createNewPrecedence(precedence);

    }

    // All UPDATE operations
    @Override
    public RegistryEntry handleUpdate(@NotNull String systemId, @NotNull Long version,
                                      @NotNull RegistryEntry incomingRegistryEntry) {
        RegistryEntry existingRegistryEntry = getRegistryEntryOrThrow(systemId);
        // Copy all the values you are allowed to copy ....
        if (null != incomingRegistryEntry.getDescription()) {
            existingRegistryEntry.setDescription(incomingRegistryEntry.getDescription());
        }
        if (null != incomingRegistryEntry.getTitle()) {
            existingRegistryEntry.setTitle(incomingRegistryEntry.getTitle());
        }
        if (null != incomingRegistryEntry.getDocumentMedium()) {
            existingRegistryEntry.setDocumentMedium(incomingRegistryEntry.getDocumentMedium());
        }
        if (null != incomingRegistryEntry.getDocumentDate()) {
            existingRegistryEntry.setDocumentDate(incomingRegistryEntry.getDocumentDate());
        }
        if (null != incomingRegistryEntry.getDueDate()) {
            existingRegistryEntry.setDueDate(incomingRegistryEntry.getDueDate());
        }
        if (null != incomingRegistryEntry.getFreedomAssessmentDate()) {
            existingRegistryEntry.setFreedomAssessmentDate(incomingRegistryEntry.getFreedomAssessmentDate());
        }
        if (null != incomingRegistryEntry.getLoanedDate()) {
            existingRegistryEntry.setLoanedDate(incomingRegistryEntry.getLoanedDate());
        }
        if (null != incomingRegistryEntry.getLoanedTo()) {
            existingRegistryEntry.setLoanedTo(incomingRegistryEntry.getLoanedTo());
        }
        existingRegistryEntry.setVersion(version);
        registryEntryRepository.save(existingRegistryEntry);
        return existingRegistryEntry;
    }

    // All DELETE operations
    @Override
    public void deleteEntity(@NotNull String registryEntrySystemId) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(registryEntrySystemId);
        registryEntryRepository.delete(registryEntry);
    }

    // All helper methods

    /**
     * Create a templated ContactInformation object
     *
     * @param correspondencePart templated correspondencePart
     */
    private void createTemplateContactInformation(IContactInformation
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
    private void createTemplateResidingAddress(IResidingAddress
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
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid RegistryEntry back. If there is no
     * valid RegistryEntry, an exception is thrown
     *
     * @param registryEntrySystemId systemId of the registryEntry to find.
     * @return the registryEntry
     */
    protected RegistryEntry getRegistryEntryOrThrow(
            @NotNull String registryEntrySystemId) {
        RegistryEntry registryEntry =
                registryEntryRepository.findBySystemId(registryEntrySystemId);
        if (registryEntry == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
                    " RegistryEntry, using systemId " + registryEntrySystemId;
            logger.info(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return registryEntry;
    }
}
