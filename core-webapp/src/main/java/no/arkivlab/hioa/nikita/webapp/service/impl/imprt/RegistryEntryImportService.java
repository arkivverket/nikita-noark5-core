package no.arkivlab.hioa.nikita.webapp.service.impl.imprt;

import nikita.model.noark5.v4.DocumentDescription;
import nikita.model.noark5.v4.Record;
import nikita.model.noark5.v4.casehandling.RegistryEntry;
import nikita.repository.n5v4.IRegistryEntryRepository;
import nikita.util.exceptions.NikitaException;
import nikita.util.exceptions.NoarkEntityNotFoundException;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.imprt.IRegistryEntryImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.TreeSet;

import static nikita.config.Constants.INFO_CANNOT_FIND_OBJECT;

@Service
@Transactional
public class RegistryEntryImportService implements IRegistryEntryImportService {

    private static final Logger logger = LoggerFactory.getLogger(RegistryEntryImportService.class);

    @Autowired
    DocumentDescriptionImportService documentDescriptionImportService;

    @Autowired
    IRegistryEntryRepository registryEntryRepository;

    public RegistryEntryImportService() {
    }

    @Override
    public RegistryEntry save(RegistryEntry registryEntry) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            throw new NikitaException("Security context problem. username is null! Cannot continue with " +
                    "this request!");
        }
        if (registryEntry.getCreatedDate() == null) {
            registryEntry.setCreatedDate(new Date());
        }
        if (registryEntry.getCreatedBy() == null) {
            registryEntry.setCreatedBy(username);
        }
        if (registryEntry.getOwnedBy() == null) {
            registryEntry.setOwnedBy(username);
        }
        registryEntry.setDeleted(false);
        registryEntryRepository.save(registryEntry);
        return registryEntry;
    }

    @Override
    public DocumentDescription createDocumentDescriptionAssociatedWithRegistryEntry(
            String systemID, DocumentDescription documentDescription) {

        DocumentDescription persistedDocumentDescription = null;
        RegistryEntry registryEntry = registryEntryRepository.findBySystemIdOrderBySystemId(systemID);
        if (registryEntry == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " RegistryEntry, using registryEntrySystemId " + systemID;
            logger.info(info) ;
            throw new NoarkEntityNotFoundException(info);
        }
        else {
            TreeSet <Record> records = (TreeSet <Record>) documentDescription.getReferenceRecord();

            if (records == null) {
                records = new TreeSet<>();
                documentDescription.setReferenceRecord(records);
            }
            records.add(registryEntry);
            persistedDocumentDescription = documentDescriptionImportService.save(documentDescription);
        }
        return persistedDocumentDescription;
    }

}
