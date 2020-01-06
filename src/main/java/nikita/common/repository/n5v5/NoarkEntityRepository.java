package nikita.common.repository.n5v5;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by tsodring on 4/2/17.
 */

@Repository
@NoRepositoryBean
public interface NoarkEntityRepository<INikitaEntity, ID extends Serializable> extends
        PagingAndSortingRepository<INikitaEntity, UUID> {

    List<INikitaEntity> findAll();

    INikitaEntity findBySystemId(UUID systemId);
}
