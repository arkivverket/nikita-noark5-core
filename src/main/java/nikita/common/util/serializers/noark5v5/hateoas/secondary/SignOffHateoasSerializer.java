package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ISignOffEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.SignOffHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing SignOff object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially the HATEOAS links and the actual
 * format of the HATEOAS links might change over time with the standard. This
 * allows us to be able to easily adapt to any changes
 */
@HateoasPacker(using = SignOffHateoasHandler.class)
@HateoasObject(using = SignOffHateoas.class)
public class SignOffHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject signOffHateoas,
                                     JsonGenerator jgen)
            throws IOException {
        ISignOffEntity signOffEntity =
            (ISignOffEntity)noarkSystemIdEntity;
        jgen.writeStartObject();

        if (signOffEntity != null) {
            printSystemIdEntity(jgen, signOffEntity);
            printDateTime(jgen, SIGN_OFF_DATE,
                      signOffEntity.getSignOffDate());
            printNullable(jgen, SIGN_OFF_BY,
                          signOffEntity.getSignOffBy());
            printNullableMetadata(jgen, SIGN_OFF_METHOD,
                                  signOffEntity.getSignOffMethod());
            // TODO handle referanseAvskrevetAv
            if (null != signOffEntity.getReferenceSignedOffRecordSystemID()) {
                print(jgen, SIGN_OFF_REFERENCE_RECORD,
                      signOffEntity.getReferenceSignedOffRecordSystemID());
            }
            if (null != signOffEntity.getReferenceSignedOffCorrespondencePartSystemID()) {
                print(jgen, SIGN_OFF_REFERENCE_CORRESPONDENCE_PART,
                      signOffEntity.getReferenceSignedOffCorrespondencePartSystemID());
            }
        }
        printHateoasLinks(jgen, signOffHateoas.getLinks(signOffEntity));
        jgen.writeEndObject();
    }
}
