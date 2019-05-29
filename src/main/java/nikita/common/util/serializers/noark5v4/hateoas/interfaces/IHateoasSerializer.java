package nikita.common.util.serializers.noark5v4.hateoas.interfaces;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;

import java.io.IOException;

/**
 * Created by tsodring on 2/9/17.
 */
public interface IHateoasSerializer {
    void serializeNoarkEntity(INikitaEntity entity, HateoasNoarkObject hateoasObject, JsonGenerator jgen)
            throws IOException;
}
