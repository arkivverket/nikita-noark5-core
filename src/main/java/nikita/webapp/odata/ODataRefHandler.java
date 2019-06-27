package nikita.webapp.odata;

import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.webapp.odata.model.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static nikita.common.util.CommonUtils.WebUtils.getEnglishNameObject;

@Service
@Transactional
public class ODataRefHandler {

    private final Logger logger =
            LoggerFactory.getLogger(ODataRefHandler.class);

    private final EntityManager entityManager;

    public ODataRefHandler(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public NoarkEntity getFromEntity(Ref ref) {
        return (NoarkEntity) entityManager.createQuery(
                buildQueryString(ref.getFromEntity())).
                setParameter(1, ref.getFromSystemId()).
                getSingleResult();
    }

    public NoarkEntity getToEntity(Ref ref) {

        Query query =
                entityManager.createQuery(
                        buildQueryString(ref.getToEntity())).
                        setParameter(1, ref.getToSystemId());
        logger.info("Query is " + query.toString());
        return (NoarkEntity) query.getSingleResult();
    }

    private String buildQueryString(String entity) {
        return "From " +
                getEnglishNameObject(entity) +
                " where systemId = ?1";
    }
}
