package nikita.common.repository.n5v5.metadata;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
@NoRepositoryBean
public interface IMetadataRepository
        <Metadata, ID extends Serializable> extends
        PagingAndSortingRepository<Metadata, String> {
    List<Metadata> findAll();

    Metadata findByCode(String code);
}
