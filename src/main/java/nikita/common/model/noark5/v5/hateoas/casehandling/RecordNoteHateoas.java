package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.RecordNoteHateoasSerializer;

import java.util.List;

/**
 * Created by tsodring on 29/05/19.
 */
@JsonSerialize(using = RecordNoteHateoasSerializer.class)
public class RecordNoteHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public RecordNoteHateoas(INoarkEntity entity) {
        super(entity);
    }

    public RecordNoteHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.REGISTRY_ENTRY);
    }

}
