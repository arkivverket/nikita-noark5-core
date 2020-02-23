package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.DocumentFlowHateoasSerializer;

import java.util.List;

import static nikita.common.config.N5ResourceMappings.DOCUMENT_FLOW;

@JsonSerialize(using = DocumentFlowHateoasSerializer.class)
public class DocumentFlowHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public DocumentFlowHateoas() {
    }

    public DocumentFlowHateoas(INoarkEntity entity) {
        super(entity);
    }

    public DocumentFlowHateoas(List<INoarkEntity> entityList) {
        super(entityList, DOCUMENT_FLOW);
    }
}
