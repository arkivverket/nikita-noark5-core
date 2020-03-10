package nikita.common.util.deserialisers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static nikita.common.config.N5ResourceMappings.FONDS_STATUS;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nikita.common.config.HATEOASConstants.LINKS;

/**
 * Created by tsodring on 1/6/17.
 * <p>
 * Deserialise an incoming Fonds JSON object.
 * <p>
 * Having a own deserialiser is done to have more fine grained control over the input. This allows us to be less strict
 * with property names, allowing for both English and Norwegian property names
 * <p>
 * <p>
 * <p>
 * Note this implementation expects that the fonds object to deserialise is in compliance with the Noark standard where
 * certain properties i.e. createdBy and createdDate are set by the core, not the caller. This deserializer will not
 * enforce this and will deserialize a fonds object correctly. This is because e.g the import interface will require
 * such functionality.
 * <p>
 * - Testing of compliance of properties is handled by the core, either in FondsController or FondsService
 * <p>
 * Note. Currently we do not include 'id' or 'deleted' properties. 'id' is a primary key and it is assumed this is
 * taken care of by the DBMS and 'deleted' is a field internal to the core to handle soft delete. Importing soft deleted
 * objects is something we do not consider necessary.
 * <p>
 * Note:
 * - Unknown property values in the JSON will trigger an exception
 * - Missing obligatory property values in the JSON will trigger an exception
 */
public class FondsDeserializer
        extends JsonDeserializer {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsDeserializer.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Fonds deserialize(JsonParser jsonParser, DeserializationContext dc)
            throws IOException {
        StringBuilder errors = new StringBuilder();

        Fonds fonds = new Fonds();
        ObjectNode objectNode = mapper.readTree(jsonParser);

        // TODO : Are we deserialising parent? No, it's not done here or is it????

        // Deserialise general properties
        CommonUtils.Hateoas.Deserialize.deserialiseNoarkGeneralEntity(fonds, objectNode, errors);

        CommonUtils.Hateoas.Deserialize.deserialiseDocumentMedium(fonds, objectNode, errors);
        CommonUtils.Hateoas.Deserialize.deserialiseStorageLocation(fonds, objectNode, errors);

        // Deserialize seriesStatus
        FondsStatus fondsStatus = (FondsStatus)
            CommonUtils.Hateoas.Deserialize.deserialiseMetadataValue(
                objectNode,
                FONDS_STATUS,
                new FondsStatus(),
                errors, false);
        fonds.setFondsStatus(fondsStatus);

        JsonNode currentNode = objectNode.get(LINKS);
        if (null != currentNode) {
            logger.debug("Payload contains " + LINKS + ". " +
                    "This value is being ignored.");
            objectNode.remove(LINKS);
        }

        // Check that there are no additional values left after processing the tree
        // If there are additional throw a malformed input exception
        if (objectNode.size() != 0) {
            errors.append("The arkiv you tried to create is malformed. The " +
                    "following fields are not recognised as arkiv fields [" +
                    CommonUtils.Hateoas.Deserialize.checkNodeObjectEmpty(objectNode) + "]. ");
        }

        if (0 < errors.length())
            throw new NikitaMalformedInputDataException(errors.toString());

        return fonds;
    }
}
