package nikita.common.model.noark5.v5.interfaces;


import nikita.common.model.noark5.v5.FondsCreator;

import java.util.Set;

/**
 * Created by tsodring on 12/7/16.
 */

public interface IFondsCreator {
    Set<FondsCreator> getReferenceFondsCreator();

    void addFondsCreator(FondsCreator fondsCreator);
}
