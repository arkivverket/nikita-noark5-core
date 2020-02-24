package nikita.webapp.util;

import nikita.common.model.noark5.v5.metadata.Metadata;
import nikita.common.repository.n5v5.metadata.IMetadataRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;
import java.util.Optional;

import static nikita.common.config.Constants.SYSTEM;
import static nikita.common.config.DatabaseConstants.METADATA_ENTITY_PACKAGE;

/**
 * Created by tsodring
 * <p>
 */
@Service
@Transactional
public class MetadataInsertTransaction {

    private WebApplicationContext appContext;
    private Repositories repositories;

    public MetadataInsertTransaction(WebApplicationContext appContext) {
        this.appContext = appContext;
        repositories = new Repositories(appContext);
    }

    public void populateMetadataEntities(String code, String codename,
                                         String fieldName)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {

        Metadata metadataEntity =
                getEntityInstance(fieldName);

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
