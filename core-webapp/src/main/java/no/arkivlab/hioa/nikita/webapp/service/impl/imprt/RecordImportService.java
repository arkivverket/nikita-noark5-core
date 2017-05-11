package no.arkivlab.hioa.nikita.webapp.service.impl.imprt;

import nikita.model.noark5.v4.DocumentDescription;
import nikita.model.noark5.v4.Record;
import nikita.repository.n5v4.IRecordRepository;
import nikita.util.exceptions.NikitaException;
import nikita.util.exceptions.NoarkEntityNotFoundException;
import no.arkivlab.hioa.nikita.webapp.service.interfaces.imprt.IRecordImportService;
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
public class RecordImportService implements IRecordImportService {

    private static final Logger logger = LoggerFactory.getLogger(RecordImportService.class);

    @Autowired
    DocumentDescriptionImportService documentDescriptionImportService;

    @Autowired
    IRecordRepository recordRepository;

    public RecordImportService() {
    }

    // All CREATE operations
    public Record save(Record record){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            throw new NikitaException("Security context problem. username is null! Cannot continue with " +
                    "this request!");
        }
        if (record.getCreatedDate() == null) {
            record.setCreatedDate(new Date());
        }
        if (record.getCreatedBy() == null) {
            record.setCreatedBy(username);
        }
        if (record.getOwnedBy() == null) {
            record.setOwnedBy(username);
        }
        record.setDeleted(false);
        return recordRepository.save(record);
    }

    @Override
    public DocumentDescription createDocumentDescriptionAssociatedWithRecord(String recordSystemId, DocumentDescription documentDescription) {
        DocumentDescription persistedDocumentDescription = null;
        Record record = recordRepository.findBySystemId(recordSystemId);
        if (record == null) {
            String info = INFO_CANNOT_FIND_OBJECT + " Record, using recordSystemId " + recordSystemId;
            logger.info(info) ;
            throw new NoarkEntityNotFoundException(info);
        }
        else {
            TreeSet <Record> records = (TreeSet <Record>) documentDescription.getReferenceRecord();

            if (records == null) {
                records = new TreeSet<>();
                documentDescription.setReferenceRecord(records);
            }
            records.add(record);
            persistedDocumentDescription = documentDescriptionImportService.save(documentDescription);
        }
        return persistedDocumentDescription;
    }
}
