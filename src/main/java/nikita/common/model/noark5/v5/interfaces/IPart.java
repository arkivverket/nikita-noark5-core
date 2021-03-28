package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Part;

import java.util.Set;

public interface IPart {
    Set<Part> getReferencePart();

    void addPart(Part part);
}
