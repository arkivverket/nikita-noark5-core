package nikita.common.util.serializers.noark5v5.hateoas;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
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
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject seriesHateoas,
                                     JsonGenerator jgen) throws IOException {
        Series series = (Series) noarkSystemIdEntity;
        jgen.writeStartObject();

        printSystemIdEntity(jgen, series);
        printTitleAndDescription(jgen, series);
        printNullableMetadata(jgen, SERIES_STATUS,
                              series.getSeriesStatus());
        printDocumentMedium(jgen, series);
        printStorageLocation(jgen, series);
        printFinaliseEntity(jgen, series);
        printCreateEntity(jgen, series);
        printModifiedEntity(jgen, series);
        printNullableDate(jgen, SERIES_START_DATE, series.getSeriesStartDate());
        printNullableDate(jgen, SERIES_END_DATE, series.getSeriesEndDate());
        if (null != series.getReferencePrecursorSystemID()) {
            print(jgen, SERIES_ASSOCIATE_AS_PRECURSOR,
                  series.getReferencePrecursorSystemID());
        }
        if (null != series.getReferenceSuccessorSystemID()) {
            print(jgen, SERIES_ASSOCIATE_AS_SUCCESSOR,
                  series.getReferenceSuccessorSystemID());
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

