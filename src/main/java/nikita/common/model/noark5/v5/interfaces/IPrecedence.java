package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Precedence;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IPrecedence {
    List<Precedence> getReferencePrecedence();

    void setReferencePrecedence(List<Precedence> precedence);
}
