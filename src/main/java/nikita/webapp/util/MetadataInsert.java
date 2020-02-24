package nikita.webapp.util;

import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

import static nikita.common.config.DatabaseConstants.METADATA_ENTITY_PACKAGE;
import static nikita.common.config.FileConstants.RESOURCE_METADATA;

/**
 * Created by tsodring
 * <p>
 */
@Service
public class MetadataInsert {

    private MetadataInsertTransaction insertTransaction;

    public MetadataInsert(MetadataInsertTransaction insertTransaction) {
        this.insertTransaction = insertTransaction;
    }

    public void populateMetadataEntities() {

        try {

            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(RESOURCE_METADATA);

            for (Resource resource : resources) {

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode tree = objectMapper.readTree(
                        Files.newBufferedReader(resource.getFile().toPath(),
                                StandardCharsets.UTF_8));

                Iterator<Map.Entry<String, JsonNode>> itr = tree.getFields();

                while (itr.hasNext()) {

                    Map.Entry mapEntry = itr.next();

                    String fieldName = (String) mapEntry.getKey();
                    JsonNode node = (JsonNode) mapEntry.getValue();

                    if (node.isArray()) {
                        Iterator<JsonNode> metadataValues = node.getElements();
                        while (metadataValues.hasNext()) {
                            JsonNode metadataObject = metadataValues.next();
                            String code = metadataObject.get("code")
                                    .getTextValue();
                            String codename = metadataObject.get("codename")
                                    .getTextValue();
                            insertTransaction.populateMetadataEntities(
                                    code, codename, fieldName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new NikitaMisconfigurationException(e.toString());
        }
    }

    private Metadata getEntityInstance(String fieldName)
            throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?> metadata = Class.forName(
                METADATA_ENTITY_PACKAGE + "." + fieldName);
        Constructor<?> constructor =
                metadata.getConstructor();
        return (Metadata) constructor.newInstance();
    }

}
