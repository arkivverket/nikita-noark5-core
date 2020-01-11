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
public interface NoarkEntityRepository<INoarkEntity, ID extends Serializable> extends
        PagingAndSortingRepository<INoarkEntity, UUID> {

    List<INoarkEntity> findAll();

    INoarkEntity findBySystemId(UUID systemId);
}
