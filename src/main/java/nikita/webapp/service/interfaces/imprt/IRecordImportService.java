package nikita.webapp.service.interfaces.imprt;


import nikita.common.model.noark5.v5.DocumentDescription;
import nikita.common.model.noark5.v5.Record;

public interface IRecordImportService {

	// -- All CREATE operations

	Record save(Record record);

    DocumentDescription createDocumentDescriptionAssociatedWithRecord(String systemID,
                                                                      DocumentDescription documentDescription);
}
