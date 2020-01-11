package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.DocumentObjectHateoasSerializer;

import java.util.List;

/**
 * Created by tsodring on 12/9/16.
 * <p>
 * Using composition rather than inheritance. Although this class is really only a placeholder for the DocumentObject object
 * along with the hateoas links. It's not intended that you will manipulate the DocumentObject object from here.
 */
@JsonSerialize(using = DocumentObjectHateoasSerializer.class)
public class DocumentObjectHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public DocumentObjectHateoas(INoarkEntity entity) {
        super(entity);
    }

    public DocumentObjectHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.DOCUMENT_OBJECT);
    }
}
