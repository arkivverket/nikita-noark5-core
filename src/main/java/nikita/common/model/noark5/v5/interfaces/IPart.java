package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Part;

import java.util.List;

public interface IPart {
    List<Part> getReferencePart();
    void setReferencePart(List<Part> part);
    void addPart(Part part);

    void removePart(Part part);
}
