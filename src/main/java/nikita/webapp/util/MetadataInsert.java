package nikita.webapp.util;

import nikita.common.model.noark5.v5.metadata.MetadataSuperClass;
import nikita.common.repository.n5v5.metadata.MetadataRepository;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static nikita.common.config.Constants.SYSTEM;
import static nikita.common.config.DatabaseConstants.METADATA_ENTITY_PACKAGE;
import static nikita.common.config.DatabaseConstants.METADATA_REPOSITORY_PACKAGE;
import static nikita.common.config.FileConstants.RESOURCE_METADATA;

/**
 * Created by tsodring
 * <p>
 */
@Service
@Transactional
public class MetadataInsert {

    private WebApplicationContext appContext;
    private Repositories repositories;

    public MetadataInsert(WebApplicationContext appContext) {
        this.appContext = appContext;
        repositories = new Repositories(appContext);
    }

    public void populateMetadataEntities() {

        try {

            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(RESOURCE_METADATA);

            for (Resource resource : resources) {

                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode tree = objectMapper.readTree(
                        new String(Files.readAllBytes(
                                resource.getFile().toPath())));

                Iterator<Map.Entry<String, JsonNode>> itr = tree.getFields();

                while (itr.hasNext()) {

                    Map.Entry mapEntry = itr.next();

                    String fieldName = (String) mapEntry.getKey();
                    JsonNode node = (JsonNode) mapEntry.getValue();

                    if (node.isArray()) {
                        Iterator<JsonNode> metadataValues = node.getElements();
                        while (metadataValues.hasNext()) {
                            JsonNode metadataObject = metadataValues.next();
                            String code = metadataObject.get("code").getTextValue();
                            String name = metadataObject.get("name").getTextValue();

                            MetadataSuperClass metadataEntity =
                                    getEntityInstance(fieldName);

                            metadataEntity.setCode(code);
                            metadataEntity.setCodeName(name);
                            metadataEntity.setSystemId(randomUUID());
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
                                MetadataRepository metadataRepository =
                                        ((MetadataRepository)
                                                repositoryOpt.get());
                                // If the code does not exist from before
                                if (null == metadataRepository.
                                        findByCode(metadataEntity.
                                                getCode())) {
                                    metadataRepository.save(metadataEntity);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new NikitaMisconfigurationException(e.toString());
        }
    }

    private MetadataSuperClass getEntityInstance(String fieldName)
            throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?> metadataEntityClass = Class.forName(
                METADATA_ENTITY_PACKAGE + "." + fieldName);
        Constructor<?> constructor =
                metadataEntityClass.getConstructor();
        return (MetadataSuperClass) constructor.newInstance();
    }

    private MetadataRepository getRepositoryInstance(String fieldName)
            throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?> metadataRepository = Class.forName(
                METADATA_REPOSITORY_PACKAGE + ".I" + fieldName + "Repository");
        Constructor<?> constructor = metadataRepository.getConstructor();
        return (MetadataRepository) constructor.newInstance();
    }
}
