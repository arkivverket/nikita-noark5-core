package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.AuthorHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.Author;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.AuthorHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.AUTHOR;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing Author object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially the HATEOAS links and the actual
 * format of the HATEOAS links might change over time with the standard. This
 * allows us to be able to easily adapt to any changes
 */
@HateoasPacker(using = AuthorHateoasHandler.class)
@HateoasObject(using = AuthorHateoas.class)
public class AuthorHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkSystemIdEntity,
                                     HateoasNoarkObject authorHateoas, JsonGenerator jgen)
            throws IOException {

        Author author = (Author) noarkSystemIdEntity;
        jgen.writeStartObject();

        printNullable(jgen, AUTHOR, author.getAuthor());

        printHateoasLinks(jgen, authorHateoas.getLinks(author));
        jgen.writeEndObject();
    }
}
