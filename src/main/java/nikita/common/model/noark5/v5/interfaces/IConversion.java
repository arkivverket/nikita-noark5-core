package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Conversion;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IConversion {
    List<Conversion> getReferenceConversion();

    void setReferenceConversion(List<Conversion> conversion);
}
