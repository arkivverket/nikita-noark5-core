package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.config.N5ResourceMappings;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.FileHateoasSerializer;

import java.util.List;

/**
 * Created by tsodring on 12/9/16.
 * <p>
 * Using composition rather than inheritance. Although this class is really only a placeholder for the File object
 * along with the hateoas links. It's not intended that you will manipulate the file object from here.
 */
@JsonSerialize(using = FileHateoasSerializer.class)
public class FileHateoas extends HateoasNoarkObject implements IHateoasNoarkObject {

    public FileHateoas(INoarkEntity entity) {
        super(entity);
    }

    public FileHateoas(List<INoarkEntity> entityList) {
        super(entityList, N5ResourceMappings.FILE);
    }

}
