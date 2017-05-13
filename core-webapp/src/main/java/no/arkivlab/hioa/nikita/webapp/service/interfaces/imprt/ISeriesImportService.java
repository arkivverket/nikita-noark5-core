package no.arkivlab.hioa.nikita.webapp.service.interfaces.imprt;

import nikita.model.noark5.v4.File;
import nikita.model.noark5.v4.Series;
import nikita.model.noark5.v4.casehandling.CaseFile;

public interface ISeriesImportService {

	// -- All CREATE operations
	File createFileAssociatedWithSeries(String seriesSystemId, File file);
	Series save(Series series);
	CaseFile createCaseFileAssociatedWithSeries(String seriesSystemId, CaseFile caseFile);
}
