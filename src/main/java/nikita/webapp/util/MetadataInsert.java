package nikita.webapp.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.repository.n5v5.metadata.IMetadataRepository;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static nikita.common.config.Constants.SYSTEM;
import static nikita.common.config.DatabaseConstants.METADATA_ENTITY_PACKAGE;
import static nikita.common.config.FileConstants.RESOURCE_METADATA;

@Service
@Transactional
public class MetadataInsert {

    private final Repositories repositories;

    public MetadataInsert(WebApplicationContext appContext) {
        repositories = new Repositories(appContext);
    }

    public void populateMetadataEntities() {
        try {
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(RESOURCE_METADATA);
            for (Resource resource : resources) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode tree = objectMapper
                        .readTree(resource.getInputStream());
                Iterator<Map.Entry<String, JsonNode>> itr = tree.fields();
                while (itr.hasNext()) {
                    Map.Entry<String, JsonNode> mapEntry = itr.next();
                    String fieldName = mapEntry.getKey();
                    JsonNode node = mapEntry.getValue();
                    if (node.isArray()) {
                        Iterator<JsonNode> metadataValues = node.elements();
                        while (metadataValues.hasNext()) {
                            JsonNode metadataObject = metadataValues.next();
                            String code = metadataObject.get("code").asText();
                            String codename = metadataObject.get("codename")
                                    .asText();
                            populateMetadataEntities(code, codename, fieldName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new NikitaMisconfigurationException(e.toString());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void populateMetadataEntities(String code, String codename,
                                         String fieldName)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {

        Metadata metadataEntity = getEntityInstance(fieldName);

        metadataEntity.setCode(code);
        metadataEntity.setCodeName(codename);
        metadataEntity.setCreatedBy(SYSTEM);
        metadataEntity.setCreatedDate(OffsetDateTime.now());
        metadataEntity.setLastModifiedBy(SYSTEM);
        metadataEntity.setLastModifiedDate(
                OffsetDateTime.now());
        metadataEntity.setOwnedBy(SYSTEM);

        Optional<Object> repositoryOpt =
                repositories.getRepositoryFor(
                        metadataEntity.getClass());

        if (repositoryOpt.isPresent()) {
            IMetadataRepository metadataRepository =
                    ((IMetadataRepository)
                            repositoryOpt.get());
            // If the code does not exist from before
            if (null == metadataRepository
                    .findByCode(metadataEntity.getCode())) {
                metadataRepository.save(metadataEntity);
            }
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
