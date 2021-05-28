package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.DocumentObjectHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.DOCUMENT_OBJECT;

@JsonSerialize(using = DocumentObjectHateoasSerializer.class)
public class DocumentObjectHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public DocumentObjectHateoas(INoarkEntity entity) {
        super(entity);
    }

    public DocumentObjectHateoas(NikitaPage page) {
        super(page, DOCUMENT_OBJECT);
    }
}
