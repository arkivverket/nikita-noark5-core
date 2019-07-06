package nikita.common.util.serializers.noark5v5.hateoas.nationalidentifier;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.nationalidentifier.PersonIdentifier;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.PersonIdentifier_CHANGE_NUMBER;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printSystemIdEntity;

public class PersonIdentifierSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INikitaEntity noarkSystemIdEntity,
            HateoasNoarkObject personIdentifierHateoas,
            JsonGenerator jgen) throws IOException {

        PersonIdentifier personIdentifier = (PersonIdentifier) noarkSystemIdEntity;
        jgen.writeStartObject();
        printSystemIdEntity(jgen, personIdentifier);
        jgen.writeNumberField(PERSON_IDENTIFIER_NUMBER,
                personIdentifier.getPersonIdentifierNumber());
        if (null != personIdentifier.getContinuousNumberingOfPersonIdentifierChange()) {
            jgen.writeNumberField(PersonIdentifier_CHANGE_NUMBER,
                    personIdentifier.getContinuousNumberingOfPersonIdentifierChange());
        }
        printHateoasLinks(jgen, personIdentifierHateoas.getLinks(personIdentifier));
        jgen.writeEndObject();
    }
}
