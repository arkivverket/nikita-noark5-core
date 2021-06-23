package nikita.common.model.noark5.v5;

import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Implement a ES / hibernate bridge for CLOB type.
 * <p>
 * ES does not understand what a CLOB is. Therefore we need a bridge that can
 * convert the CLOB to a string type for indexing.
 * <p>
 * The conversion of CLOB to String is taken from:
 * https://stackoverflow.com/questions/22874722/clob-to-string-bufferedreader-vs-getsubstring
 */
public class ClobImpl
        implements ValueBridge<Clob, String> {

    private final Logger logger = LoggerFactory.getLogger(ClobImpl.class);

    @Override
    public String toIndexedValue(Clob clob,
                                 ValueBridgeToIndexedValueContext context) {
        if (null != clob) {
            StringBuilder sb = new StringBuilder();
            try {
                char[] buffer = new char[8092];
                int l;
                Reader r = clob.getCharacterStream();
                while ((l = r.read(buffer)) > -1) {
                    sb.append(buffer, 0, l);
                }
            } catch (SQLException | IOException e) {
                logger.error("Problem when trying to index CLOB value" + e);
            }
            return sb.toString();
        }
        return null;
    }
}
