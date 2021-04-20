package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Part;

import javax.validation.constraints.NotNull;
import java.util.Set;

public interface IPart {
    Set<Part> getReferencePart();

    void addPart(@NotNull Part part);

    void removePart(@NotNull Part part);
}
