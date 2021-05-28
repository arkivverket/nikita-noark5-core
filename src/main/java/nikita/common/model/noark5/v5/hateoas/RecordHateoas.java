package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.RecordHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.RECORD;

@JsonSerialize(using = RecordHateoasSerializer.class)
public class RecordHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public RecordHateoas(INoarkEntity entity) {
        super(entity);
    }

    public RecordHateoas(NikitaPage page) {
        super(page, RECORD);
    }
}
