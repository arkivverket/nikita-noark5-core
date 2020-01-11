package nikita.common.repository.n5v5.metadata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tsodring on 4/2/17.
 */

@Repository
@NoRepositoryBean
public interface MetadataRepository
        <INoarkEntity, ID extends Serializable> extends
        CrudRepository<INoarkEntity, String> {

    List<INoarkEntity> findAll();

    INoarkEntity findByCode(String code);
}
