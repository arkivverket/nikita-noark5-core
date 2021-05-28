package nikita.common.model.noark5.v5.hateoas;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nikita.common.model.nikita.NikitaPage;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.util.serializers.noark5v5.hateoas.FileHateoasSerializer;

import static nikita.common.config.N5ResourceMappings.FILE;

@JsonSerialize(using = FileHateoasSerializer.class)
public class FileHateoas
        extends HateoasNoarkObject
        implements IHateoasNoarkObject {

    public FileHateoas(INoarkEntity entity) {
        super(entity);
    }

    public FileHateoas(NikitaPage page) {
        super(page, FILE);
    }
}
