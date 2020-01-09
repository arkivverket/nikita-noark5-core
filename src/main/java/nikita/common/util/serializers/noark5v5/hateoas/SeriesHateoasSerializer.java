package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing Series object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially control the HATEOAS links and the
 * actual format of the HATEOAS links might change over time with the standard.
 * This allows us to be able to easily adapt to any changes
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' are not exported
 */
public class SeriesHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject seriesHateoas,
                                     JsonGenerator jgen) throws IOException {
        Series series = (Series) noarkSystemIdEntity;
        jgen.writeStartObject();

        printNikitaEntity(jgen, series);
        printTitleAndDescription(jgen, series);
        if (series.getSeriesStatusCode() != null) {
            jgen.writeObjectFieldStart(SERIES_STATUS);
            printCode(jgen,
                      series.getSeriesStatusCode(),
                      series.getSeriesStatusCodeName());
            jgen.writeEndObject();
        }
        printDocumentMedium(jgen, series);
        printStorageLocation(jgen, series);
        printFinaliseEntity(jgen, series);
        if (series.getSeriesStartDate() != null) {
            jgen.writeStringField(SERIES_START_DATE,
                    Serialize.formatDate(series.getSeriesStartDate()));
        }
        if (series.getSeriesEndDate() != null) {
            jgen.writeStringField(SERIES_END_DATE,
                    Serialize.formatDate(series.getSeriesEndDate()));
        }
        if (series.getReferencePrecursor() != null &&
                series.getReferencePrecursor().getSystemId() != null) {
            jgen.writeStringField(SERIES_PRECURSOR,
                    series.getReferencePrecursor().getSystemId());
        }
        if (series.getReferenceSuccessor() != null &&
                series.getReferenceSuccessor().getSystemId() != null) {
            jgen.writeStringField(SERIES_SUCCESSOR,
                    series.getReferenceSuccessor().getSystemId());
        }
        printDisposal(jgen, series);
        printDisposalUndertaken(jgen, series);
        printDeletion(jgen, series);
        printScreening(jgen, series);
        printClassified(jgen, series);
        printHateoasLinks(jgen, seriesHateoas.getLinks(series));
        jgen.writeEndObject();
    }
}

