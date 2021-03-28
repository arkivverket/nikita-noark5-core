package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;

import java.util.List;

public interface ICorrespondencePart {
    List<CorrespondencePart> getReferenceCorrespondencePart();
    void setReferenceCorrespondencePart(List<CorrespondencePart> part);
    void addCorrespondencePart(CorrespondencePart part);

    void removeCorrespondencePart(CorrespondencePart part);
}
