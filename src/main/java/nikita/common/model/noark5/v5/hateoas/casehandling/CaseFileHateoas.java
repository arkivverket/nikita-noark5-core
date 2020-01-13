package nikita.common.model.noark5.v5.hateoas.casehandling;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.casehandling.CaseFileHateoasSerializer;

import java.util.List;

/**
 * Created by tsodring on 12/9/16.
 * <p>
 * Using composition rather than inheritance. Although this class is really only a placeholder for the File object
 * along with the hateoas links. It's not intended that you will manipulate the file object from here.
 */
@JsonSerialize(using = CaseFileHateoasSerializer.class)
public class CaseFileHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public CaseFileHateoas(INoarkEntity entity) {
        super(entity);
    }

    public CaseFileHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.CASE_FILE);
    }
}
