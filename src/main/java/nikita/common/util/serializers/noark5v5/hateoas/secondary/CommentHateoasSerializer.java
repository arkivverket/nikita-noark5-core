package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.CommentHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.secondary.ICommentEntity;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.CommentHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

/**
 * Serialise an outgoing Comment object as JSON.
 * <p>
 * Having an own serializer is done to have more fine grained control over the
 * output. We need to be able to especially the HATEOAS links and the actual
 * format of the HATEOAS links might change over time with the standard. This
 * allows us to be able to easily adapt to any changes
 */
@HateoasPacker(using = CommentHateoasHandler.class)
@HateoasObject(using = CommentHateoas.class)
public class CommentHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(INoarkEntity noarkEntity,
                                     HateoasNoarkObject commentHateoas,
                                     JsonGenerator jgen)
            throws IOException {
        ICommentEntity comment = (ICommentEntity)noarkEntity;
        jgen.writeStartObject();

        if (comment != null) {
            printSystemIdEntity(jgen, comment);
            printNullable(jgen, COMMENT_TEXT, comment.getCommentText());
            printNullableMetadata(jgen, COMMENT_TYPE, comment.getCommentType());
            printDateTime(jgen, COMMENT_DATE, comment.getCommentDate());
            printNullable(jgen, COMMENT_REGISTERED_BY,
                          comment.getCommentRegisteredBy());
        }
        printHateoasLinks(jgen, commentHateoas.getLinks(comment));
        jgen.writeEndObject();
    }
}
