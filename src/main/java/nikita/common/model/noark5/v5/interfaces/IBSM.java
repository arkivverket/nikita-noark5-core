package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.bsm.BSMBase;

import java.util.List;

public interface IBSM {
    List<BSMBase> getReferenceBSMBase();
    void setReferenceBSMBase(List<BSMBase> bSMBases);
    void addBSMBase(BSMBase bSMBase);

    void removeBSMBase(BSMBase bSMBase);
}
