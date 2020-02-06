package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Part;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IPart {
    List<Part> getReferencePart();

    void setReferencePart(List<Part> part);

    void addPart(Part part);
}
