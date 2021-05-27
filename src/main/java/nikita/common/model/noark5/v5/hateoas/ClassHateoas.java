package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.ClassHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.CLASS;

@JsonSerialize(using = ClassHateoasSerializer.class)
public class ClassHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ClassHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ClassHateoas(NikitaPage page) {
        super(page, CLASS);
    }
}
