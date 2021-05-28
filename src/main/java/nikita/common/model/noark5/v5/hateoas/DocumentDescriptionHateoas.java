package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.DocumentDescriptionHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.DOCUMENT_DESCRIPTION;

@JsonSerialize(using = DocumentDescriptionHateoasSerializer.class)
public class DocumentDescriptionHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public DocumentDescriptionHateoas(INoarkEntity entity) {
        super(entity);
    }

    public DocumentDescriptionHateoas(NikitaPage page) {
        super(page, DOCUMENT_DESCRIPTION);
    }
}
