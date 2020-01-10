package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.ConversionHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.IConversionEntity;
import nikita.common.model.noark5.v5.secondary.Conversion;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.ConversionHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing Conversion object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially the HATEOAS links and the actual
 * format of the HATEOAS links might change over time with the standard. This
 * allows us to be able to easily adapt to any changes
 */
@HateoasPacker(using = ConversionHateoasHandler.class)
@HateoasObject(using = ConversionHateoas.class)
public class ConversionHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INikitaEntity noarkSystemIdEntity,
                                     HateoasNoarkObject conversionHateoas,
                                     JsonGenerator jgen)
            throws IOException {
        IConversionEntity conversionEntity =
            (IConversionEntity)noarkSystemIdEntity;
        jgen.writeStartObject();

        if (conversionEntity != null) {
            printSystemIdEntity(jgen, conversionEntity);

            jgen.writeStringField(CONVERTED_DATE,
                formatDate(conversionEntity.getConvertedDate()));

            if (null != conversionEntity.getConvertedBy()) {
                jgen.writeStringField(CONVERTED_BY,
                    conversionEntity.getConvertedBy());
            }

            if (null != conversionEntity.getConvertedFromFormat()) {
                jgen.writeStringField(CONVERTED_FROM_FORMAT,
                    conversionEntity.getConvertedFromFormat());
            }

            if (null != conversionEntity.getConvertedToFormat()) {
                jgen.writeStringField(CONVERTED_TO_FORMAT,
                    conversionEntity.getConvertedToFormat());
            }

            if (null != conversionEntity.getConversionTool()) {
                jgen.writeStringField(CONVERSION_TOOL,
                    conversionEntity.getConversionTool());
            }

            if (null != conversionEntity.getConversionComment()) {
                jgen.writeStringField(CONVERSION_COMMENT,
                                      conversionEntity.getConversionComment());
            }
        }
        printHateoasLinks(jgen, conversionHateoas.getLinks(conversionEntity));
        jgen.writeEndObject();
    }
}
