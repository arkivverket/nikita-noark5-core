package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;

import java.util.List;

/**
 * Created by tsodring on 5/22/17.
 */
public interface ICorrespondencePart {

    List<CorrespondencePart> getReferenceCorrespondencePart();

    void setReferenceCorrespondencePart(List<CorrespondencePart> part);

    void addCorrespondencePart(CorrespondencePart part);
}
