package nikita.common.repository.n5v5.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
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
        <IMetadataEntity, ID extends Serializable> extends
        CrudRepository<IMetadataEntity, String> {

    List<IMetadataEntity> findAll();

    IMetadataEntity findByCode(String code);
}
