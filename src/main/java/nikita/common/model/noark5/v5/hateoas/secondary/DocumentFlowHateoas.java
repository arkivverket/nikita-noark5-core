package nikita.common.model.noark5.v5.hateoas.secondary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.secondary.DocumentFlowHateoasSerializer;

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

    public DocumentFlowHateoas(NikitaPage page) {
        super(page, DOCUMENT_FLOW);
    }
}
