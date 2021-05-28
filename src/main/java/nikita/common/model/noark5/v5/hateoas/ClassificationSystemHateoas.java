package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.ClassificationSystemHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.CLASSIFICATION_SYSTEM;

@JsonSerialize(using = ClassificationSystemHateoasSerializer.class)
public class ClassificationSystemHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ClassificationSystemHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ClassificationSystemHateoas(NikitaPage page) {
        super(page, CLASSIFICATION_SYSTEM);
    }
}
