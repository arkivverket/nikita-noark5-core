package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.SeriesStatus;
import nikita.webapp.service.interfaces.metadata.IMetadataSuperService;

import java.util.List;

/**
 * Created by tsodring on 3/9/17.
 */
public interface ISeriesStatusService
    extends IMetadataSuperService {

    SeriesStatus createNewSeriesStatus(SeriesStatus seriesStatus);

    List<SeriesStatus> findAll();

    SeriesStatus update(SeriesStatus seriesStatus);
}
