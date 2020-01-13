package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.SeriesStatus;

import java.util.List;

/**
 * Created by tsodring on 3/9/17.
 */
public interface ISeriesStatusService {

    SeriesStatus createNewSeriesStatus(SeriesStatus seriesStatus);

    List<SeriesStatus> findAll();

    SeriesStatus findByCode(String code);

    SeriesStatus findSeriesStatusByCode(String code);

    SeriesStatus update(SeriesStatus seriesStatus);
}
