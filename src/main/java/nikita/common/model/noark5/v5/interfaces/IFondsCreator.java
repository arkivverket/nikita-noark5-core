package nikita.common.model.noark5.v5.interfaces;


import nikita.common.model.noark5.v5.FondsCreator;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */

public interface IFondsCreator {
    List<FondsCreator> getReferenceFondsCreator();

    void setReferenceFondsCreator(List<FondsCreator> referenceFondsCreator);
}
