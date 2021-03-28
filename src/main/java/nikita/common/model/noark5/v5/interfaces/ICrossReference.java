package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.CrossReference;

import java.util.List;

public interface ICrossReference {
    List<CrossReference> getReferenceCrossReference();

    void setReferenceCrossReference(List<CrossReference> crossReference);

    void addCrossReference(CrossReference crossReference);

    void removeCrossReference(CrossReference crossReference);
}
