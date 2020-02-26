package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.DocumentFlowHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.DocumentFlow;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.DocumentFlowHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

@HateoasPacker(using = DocumentFlowHateoasHandler.class)
@HateoasObject(using = DocumentFlowHateoas.class)
public class DocumentFlowHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity
	(INoarkEntity noarkSystemIdEntity,
	 HateoasNoarkObject documentFlowHateoas, JsonGenerator jgen)
            throws IOException {

        DocumentFlow documentFlow = (DocumentFlow) noarkSystemIdEntity;
        jgen.writeStartObject();

        printNullable(jgen, DOCUMENT_FLOW_FLOW_TO,
                      documentFlow.getFlowTo());
        printNullable(jgen, DOCUMENT_FLOW_FLOW_FROM,
                      documentFlow.getFlowFrom());
        printNullableDateTime(jgen, DOCUMENT_FLOW_FLOW_RECEIVED_DATE,
                              documentFlow.getFlowReceivedDate());
        printNullableDateTime(jgen, DOCUMENT_FLOW_FLOW_SENT_DATE,
                              documentFlow.getFlowSentDate());
        printNullableMetadata(jgen, DOCUMENT_FLOW_FLOW_STATUS,
                              documentFlow.getFlowStatus());
        printNullable(jgen, DOCUMENT_FLOW_FLOW_COMMENT,
                      documentFlow.getFlowComment());

        printHateoasLinks(jgen, documentFlowHateoas.getLinks(documentFlow));
        jgen.writeEndObject();
    }
}
