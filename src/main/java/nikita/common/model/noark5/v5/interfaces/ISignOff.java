package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.SignOff;

import java.util.Set;

public interface ISignOff {
    Set<SignOff> getReferenceSignOff();

    void addSignOff(SignOff signOff);
}
