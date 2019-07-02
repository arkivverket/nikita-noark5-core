package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.metadata.SeriesStatus;

import java.util.List;
import java.util.UUID;

/**
 * Created by tsodring on 3/9/17.
 */
public interface ISeriesStatusService {

    SeriesStatus createNewSeriesStatus(SeriesStatus seriesStatus);

    Iterable<SeriesStatus> findAll();

    SeriesStatus findBySystemId(UUID systemId);

    SeriesStatus update(SeriesStatus seriesStatus);

    List<SeriesStatus> findByDescription(String description);

    List<SeriesStatus> findByCode(String code);
}
