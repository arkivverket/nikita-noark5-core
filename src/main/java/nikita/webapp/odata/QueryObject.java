package nikita.webapp.odata;

import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import org.hibernate.query.Query;

public class QueryObject {

    private final Query<INoarkEntity> query;
    private final String fromEntity;

    public QueryObject(Query<INoarkEntity> query, String fromEntity) {
        this.query = query;
        this.fromEntity = fromEntity;
    }

    public Query<INoarkEntity> getQuery() {
        return query;
    }

    public String getFromEntity() {
        return fromEntity;
    }
}
