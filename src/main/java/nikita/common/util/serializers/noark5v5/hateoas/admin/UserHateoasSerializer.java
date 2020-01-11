package nikita.common.util.serializers.noark5v5.hateoas.admin;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.CommonUtils;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;

/**
 * Serialise an outgoing UserHateoas object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially control the HATEOAS links and the
 * actual format of the HATEOAS links might change over time with the standard.
 * This allows us to be able to easily adapt to any changes.
 * <p>
 * Note. Only values that are part of the standard are included in the JSON.
 * Properties like 'id' or 'deleted' are not exported
 */
public class UserHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject userHateoas,
                                     JsonGenerator jgen) throws IOException {
        User user = (User) noarkSystemIdEntity;

        jgen.writeStartObject();
        CommonUtils.Hateoas.Serialize.printNikitaEntity(jgen, user);
        if (user.getUsername() != null) {
            jgen.writeStringField(USER_NAME, user.getUsername());
        }
        if (user.getFirstname() != null) {
            jgen.writeStringField(FIRST_NAME, user.getFirstname());
        }
        if (user.getLastname() != null) {
            jgen.writeStringField(SECOND_NAME, user.getLastname());
        }
        CommonUtils.Hateoas.Serialize.printFinaliseEntity(jgen, user);
        CommonUtils.Hateoas.Serialize.printHateoasLinks(jgen, userHateoas.getLinks(user));
        jgen.writeEndObject();
    }
}
