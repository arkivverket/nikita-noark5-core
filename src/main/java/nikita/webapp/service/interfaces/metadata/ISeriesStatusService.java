package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.SeriesStatus;

/**
 * Created by tsodring on 3/9/17.
 */
public interface ISeriesStatusService {

    SeriesStatus createNewSeriesStatus(SeriesStatus seriesStatus);

    Iterable<SeriesStatus> findAll();

    SeriesStatus update(SeriesStatus seriesStatus);

    SeriesStatus findByCode(String code);
}
