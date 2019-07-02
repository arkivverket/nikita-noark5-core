package nikita.common.repository.n5v5.metadata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by tsodring on 4/2/17.
 */

@Repository
@NoRepositoryBean
public interface UniqueCodeMetadataRepository<INikitaEntity, ID extends Serializable> extends
        CrudRepository<INikitaEntity, Long> {

    List<INikitaEntity> findAll();

    INikitaEntity findBySystemId(UUID systemId);

    List<INikitaEntity> findByDescription(String description);

    INikitaEntity findByCode(String code);
}
