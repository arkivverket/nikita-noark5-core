package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.DocumentDescription;
import nikita.common.model.noark5.v4.Record;
import nikita.common.model.noark5.v4.casehandling.Precedence;
import nikita.common.model.noark5.v4.casehandling.RegistryEntry;
import nikita.common.model.noark5.v4.casehandling.secondary.ContactInformation;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v4.interfaces.ICorrespondencePart;
import nikita.common.model.noark5.v4.metadata.CorrespondencePartType;
import nikita.common.repository.n5v4.IRegistryEntryRepository;
import nikita.common.repository.n5v4.metadata.ICorrespondencePartTypeRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.interfaces.IRegistryEntryService;
import nikita.webapp.service.interfaces.secondary.ICorrespondencePartService;
import nikita.webapp.service.interfaces.secondary.IPrecedenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static nikita.common.config.Constants.INFO_CANNOT_FIND_OBJECT;
import static nikita.webapp.util.NoarkUtils.NoarkEntity.Create.*;

@Service
@Transactional
public class RegistryEntryService
        implements IRegistryEntryService {

    private static final Logger logger = LoggerFactory.getLogger(RegistryEntryService.class);
    //@Value("${nikita-noark5-core.pagination.maxPageSize}")
    Integer maxPageSize = 10;
    private DocumentDescriptionService documentDescriptionService;
    private ICorrespondencePartService correspondencePartService;
    private IPrecedenceService precedenceService;
    private IRegistryEntryRepository registryEntryRepository;
    private ICorrespondencePartTypeRepository correspondencePartTypeRepository;
    private EntityManager entityManager;

    public RegistryEntryService(DocumentDescriptionService documentDescriptionService,
                                ICorrespondencePartService correspondencePartService,
                                IPrecedenceService precedenceService,
                                IRegistryEntryRepository registryEntryRepository,
                                ICorrespondencePartTypeRepository correspondencePartTypeRepository,
                                EntityManager entityManager) {
        this.documentDescriptionService = documentDescriptionService;
        this.correspondencePartService = correspondencePartService;
        this.precedenceService = precedenceService;
        this.registryEntryRepository = registryEntryRepository;
        this.correspondencePartTypeRepository = correspondencePartTypeRepository;
        this.entityManager = entityManager;
    }

    @Override
    public RegistryEntry save(@NotNull RegistryEntry registryEntry) {
        setNikitaEntityValues(registryEntry);
        setSystemIdEntityValues(registryEntry);
        setCreateEntityValues(registryEntry);
        checkDocumentMediumValid(registryEntry);
        registryEntry.setRecordDate(new Date());
        registryEntryRepository.save(registryEntry);
        return registryEntry;
    }

    private void associateCorrespondencePartTypeWithCorrespondencePart(
            @NotNull ICorrespondencePart correspondencePart) {
        CorrespondencePartType incomingCorrespondencePartType =
                correspondencePart.getCorrespondencePartType();
        // It should never get this far with a null value
        // It should be rejected at controller level
        // The incoming CorrespondencePartType will not have @id field set. Therefore, we have to look it up
        // in the database and make sure the proper CorrespondencePartType is associated with the CorrespondencePart
        if (incomingCorrespondencePartType != null && incomingCorrespondencePartType.getCode() != null) {
            CorrespondencePartType actualCorrespondencePartType =
                    correspondencePartTypeRepository.findByCode(incomingCorrespondencePartType.getCode());
            if (actualCorrespondencePartType != null) {
                correspondencePart.setCorrespondencePartType(actualCorrespondencePartType);
            }
        }
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
             String systemID, CorrespondencePartPerson correspondencePart) {
         RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);

        CorrespondencePartType incomingCorrespondencePartType =
                correspondencePart.getCorrespondencePartType();
        // It should never get this far with a null value
        // It should be rejected at controller level
        // The incoming CorrespondencePartType will not have @id field set. Therefore, we have to look it up
        // in the database and make sure the proper CorrespondencePartType is associated with the CorrespondencePart
        if (incomingCorrespondencePartType != null && incomingCorrespondencePartType.getCode() != null) {
            CorrespondencePartType actualCorrespondencePartType =
                    correspondencePartTypeRepository.findByCode(incomingCorrespondencePartType.getCode());
            if (actualCorrespondencePartType != null) {
                correspondencePart.setCorrespondencePartType(actualCorrespondencePartType);
            }
        }

         /*
         ZZXC
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
         }
         correspondencePart.setPostalAddress(postalAddress);

         ResidingAddress residingAddress = correspondencePart.getResidingAddress();
         if (null != residingAddress) {
             setNikitaEntityValues(residingAddress);
             setSystemIdEntityValues(residingAddress);
         }
         correspondencePart.setResidingAddress(residingAddress);

         setNikitaEntityValues(correspondencePart);
         setSystemIdEntityValues(correspondencePart);
         */
         // bidirectional relationship @ManyToMany, set both sides of relationship
         registryEntry.getReferenceCorrespondencePartPerson().add(correspondencePart);
        correspondencePart.addRegistryEntry(registryEntry);
         return correspondencePartService.createNewCorrespondencePartPerson(correspondencePart);
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
    public CorrespondencePartUnit
    createCorrespondencePartUnitAssociatedWithRegistryEntry(
            String systemID, CorrespondencePartUnit correspondencePart) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);


        CorrespondencePartType incomingCorrespondencePartType =
                correspondencePart.getCorrespondencePartType();
        // It should never get this far with a null value
        // It should be rejected at controller level
        // The incoming CorrespondencePartType will not have @id field set. Therefore, we have to look it up
        // in the database and make sure the proper CorrespondencePartType is associated with the CorrespondencePart
        if (incomingCorrespondencePartType != null && incomingCorrespondencePartType.getCode() != null) {
            CorrespondencePartType actualCorrespondencePartType =
                    correspondencePartTypeRepository.findByCode(incomingCorrespondencePartType.getCode());
            if (actualCorrespondencePartType != null) {
                correspondencePart.setCorrespondencePartType(actualCorrespondencePartType);
            }
        }

        ContactInformation contactInformation = correspondencePart.getContactInformation();
        if (null != contactInformation) {
        }
        correspondencePart.setContactInformation(contactInformation);
        contactInformation.setCorrespondencePartUnit(correspondencePart);
/*
        PostalAddress postalAddress = correspondencePart.getPostalAddress();
        if (null != postalAddress) {
            setNikitaEntityValues(postalAddress);
            setSystemIdEntityValues(postalAddress);
        }
        correspondencePart.setPostalAddress(postalAddress);

ZZXC

        SimpleAddress businessAddress = correspondencePart.getBusinessAddress();
        if (null != businessAddress) {
            setNikitaEntityValues(businessAddress);
            setSystemIdEntityValues(businessAddress);
        }
*/
        setNikitaEntityValues(correspondencePart);
        setSystemIdEntityValues(correspondencePart);
        // bidirectional relationship @ManyToMany, set both sides of relationship
        registryEntry.getReferenceCorrespondencePartUnit().add(correspondencePart);
        correspondencePart.getReferenceRegistryEntry().add(registryEntry);
        return correspondencePartService.createNewCorrespondencePartUnit(correspondencePart);
    }


    @Override
    public DocumentDescription createDocumentDescriptionAssociatedWithRegistryEntry(
            String systemID, DocumentDescription documentDescription) {
        RegistryEntry registryEntry = getRegistryEntryOrThrow(systemID);
        ArrayList<Record> records = (ArrayList<Record>) documentDescription.getReferenceRecord();

        // It should always be instaniated ... check this ...
        if (records == null) {
            records = new ArrayList<>();
            documentDescription.setReferenceRecord(records);
        }
        records.add(registryEntry);
        return documentDescriptionService.save(documentDescription);
    }

    // All READ operations
    public List<RegistryEntry> findRegistryEntryByOwnerPaginated(Integer top, Integer skip) {

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
            existingRegistryEntry.setDueDate(incomingRegistryEntry.getDueDate() );
        }
        if (null != incomingRegistryEntry.getFreedomAssessmentDate()) {
            existingRegistryEntry.setFreedomAssessmentDate(incomingRegistryEntry.getFreedomAssessmentDate());
        }
        if (null != incomingRegistryEntry.getLoanedDate()) {
            existingRegistryEntry.setLoanedDate(incomingRegistryEntry.getLoanedDate() );
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
