package nikita.common.util;

import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class allows for an application specific approach to generating UUID
 * automatically without having randomUUID litered throughout the code. UUID
 * generation is handled in one place. It was introduced because the hibernate
 * approach was not allowing us to manually set a UUID if required.
 * <p>
 * SuppressWarnings("unused") as IDE is not able to see class use.
 */
@SuppressWarnings("unused")
public class NikitaUUIDGenerator
        extends UUIDGenerator {
    /**
     * Generate a UUID that can be used as a primary key. If the object
     * already has a primary key, just use the existing primary key.
     *
     * @param session The session
     * @param object  The object requiring a UUID / primary key
     * @return the UUID value
     * @throws HibernateException if there is a problem
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws HibernateException {
        try {
            final UUID uuid = (UUID) super.generate(session, object);
            if (((ISystemId) object).getSystemId() == null) {
                return uuid;
            } else return ((ISystemId) object).getSystemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
