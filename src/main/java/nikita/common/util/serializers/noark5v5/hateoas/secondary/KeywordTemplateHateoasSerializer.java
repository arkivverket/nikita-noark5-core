package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.Keyword;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.KEYWORD;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing Keyword object as JSON.
 */
public class KeywordTemplateHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject keywordHateoas, JsonGenerator jgen)
            throws IOException {
        Keyword keyword = (Keyword) noarkSystemIdEntity;
        jgen.writeStartObject();
        printNullable(jgen, KEYWORD, keyword.getKeyword());
        printHateoasLinks(jgen, keywordHateoas.getLinks(keyword));
        jgen.writeEndObject();
    }
}
