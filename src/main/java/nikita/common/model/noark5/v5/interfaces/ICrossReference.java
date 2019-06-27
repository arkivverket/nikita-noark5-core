package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.CrossReference;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */

public interface ICrossReference {
    List<CrossReference> getReferenceCrossReference();
    void setReferenceCrossReference(List<CrossReference> crossReference);

    void addReferenceCrossReference(CrossReference crossReference);
}
