package nikita.webapp.service.interfaces.imprt;

import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;


public interface ICaseFileImportService extends IFileImportService{
	CaseFile save(CaseFile caseFile);
	RegistryEntry createRegistryEntryAssociatedWithCaseFile(String fileSystemId, RegistryEntry registryEntry);
}
