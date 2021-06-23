package nikita.common.model.noark5.v5;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

import java.sql.Clob;

public class ClobImpl implements
        ValueBridge<Clob, String> {
    @Override
    public String toIndexedValue(Clob clob,
                                 ValueBridgeToIndexedValueContext context) {
        return clob == null ? null : clob.toString();
    }
}
