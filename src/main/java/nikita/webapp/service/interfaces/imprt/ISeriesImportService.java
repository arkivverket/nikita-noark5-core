package nikita.webapp.service.interfaces.imprt;

import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.casehandling.CaseFile;

public interface ISeriesImportService {

	// -- All CREATE operations
	File createFileAssociatedWithSeries(String seriesSystemId, File file);
	Series save(Series series);
	CaseFile createCaseFileAssociatedWithSeries(String seriesSystemId, CaseFile caseFile);
}
