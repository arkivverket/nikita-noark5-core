package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.Deletion;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IDeletion {
    Deletion getReferenceDeletion();

    void setReferenceDeletion(Deletion deletion);
}
