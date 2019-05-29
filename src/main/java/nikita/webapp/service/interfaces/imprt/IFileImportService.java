package nikita.webapp.service.interfaces.imprt;

import nikita.common.model.noark5.v5.BasicRecord;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;

public interface IFileImportService {
	// -- All CREATE operations
	File createFile(File file);
	Record createRecordAssociatedWithFile(String fileSystemId, Record record);
	BasicRecord createBasicRecordAssociatedWithFile(String fileSystemId, BasicRecord basicRecord);
}
