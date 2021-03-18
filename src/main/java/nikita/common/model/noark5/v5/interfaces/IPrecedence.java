package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Precedence;

import java.util.Set;

public interface IPrecedence {
    Set<Precedence> getReferencePrecedence();

    void addPrecedence(Precedence precedence);
}
