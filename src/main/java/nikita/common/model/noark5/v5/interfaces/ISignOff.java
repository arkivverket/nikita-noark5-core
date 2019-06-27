package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.SignOff;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */
public interface ISignOff {
    List<SignOff> getReferenceSignOff();

    void setReferenceSignOff(List<SignOff> signOff);
}
