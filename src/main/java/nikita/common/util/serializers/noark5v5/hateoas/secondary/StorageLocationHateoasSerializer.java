package nikita.common.util.serializers.noark5v5.hateoas.secondary;

import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v5.hateoas.secondary.StorageLocationHateoas;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.secondary.StorageLocation;
import nikita.common.util.serializers.noark5v5.hateoas.HateoasSerializer;
import nikita.common.util.serializers.noark5v5.hateoas.interfaces.IHateoasSerializer;
import nikita.webapp.hateoas.secondary.StorageLocationHateoasHandler;
import nikita.webapp.util.annotation.HateoasObject;
import nikita.webapp.util.annotation.HateoasPacker;

import java.io.IOException;

import static nikita.common.config.N5ResourceMappings.STORAGE_LOCATION;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printHateoasLinks;

/**
 * Serialise an outgoing StorageLocation object as JSON.
 */
@HateoasPacker(using = StorageLocationHateoasHandler.class)
@HateoasObject(using = StorageLocationHateoas.class)
public class StorageLocationHateoasSerializer
        extends HateoasSerializer
        implements IHateoasSerializer {

    @Override
    public void serializeNoarkEntity(
            INoarkEntity noarkSystemIdEntity,
            HateoasNoarkObject storageLocationHateoas, JsonGenerator jgen)
            throws IOException {
        StorageLocation storageLocation = (StorageLocation) noarkSystemIdEntity;
        jgen.writeStartObject();
        printNullable(jgen, STORAGE_LOCATION, storageLocation.getStorageLocation());
        printHateoasLinks(jgen, storageLocationHateoas.getLinks(storageLocation));
        jgen.writeEndObject();
    }
}
