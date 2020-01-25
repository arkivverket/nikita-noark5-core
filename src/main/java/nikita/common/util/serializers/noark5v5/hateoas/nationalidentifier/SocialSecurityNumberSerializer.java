package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.SOCIAL_SECURITY_NUMBER;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class SocialSecurityNumberSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject socialSecurityNumberHateoas,
            JsonGenerator jgen) throws IOException {

        SocialSecurityNumber socialSecurityNumber =
                (SocialSecurityNumber) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, socialSecurityNumber);
        printNullable(jgen, SOCIAL_SECURITY_NUMBER,
                      socialSecurityNumber.getSocialSecurityNumber());
        printHateoasLinks(jgen, socialSecurityNumberHateoas.
                getLinks(socialSecurityNumber));
        jgen.writeEndObject();
    }
}
