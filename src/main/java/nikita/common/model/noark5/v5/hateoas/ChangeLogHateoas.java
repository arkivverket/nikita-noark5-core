package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.ChangeLogHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.CHANGE_LOG;

@JsonSerialize(using = ChangeLogHateoasSerializer.class)
public class ChangeLogHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public ChangeLogHateoas(INoarkEntity entity) {
        super(entity);
    }

    public ChangeLogHateoas(NikitaPage page) {
        super(page, CHANGE_LOG);
    }
}
