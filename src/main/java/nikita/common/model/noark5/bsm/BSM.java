package nikita.common.model.noark5.bsm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import nikita.common.model.noark5.v5.interfaces.IBSM;
import nikita.common.util.deserialisers.BSMDeserialiser;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = BSMDeserialiser.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BSM
        implements IBSM {

    private final List<BSMBase> bsm = new ArrayList<>();

    @Override
    public List<BSMBase> getReferenceBSMBase() {
        return bsm;
    }

    @Override
    public void addReferenceBSMBase(List<BSMBase> bSMBases) {
        this.bsm.addAll(bSMBases);
    }

    @Override
    public void addBSMBase(BSMBase bSMBase) {
        this.bsm.add(bSMBase);
    }

    @Override
    public void removeBSMBase(BSMBase bSMBase) {
        this.bsm.remove(bSMBase);
    }
}
