package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.secondary.ElectronicSignature;

/**
 * Created by tsodring on 12/7/16.
 */
public interface IElectronicSignature {
    ElectronicSignature getReferenceElectronicSignature();

    void setReferenceElectronicSignature(ElectronicSignature electronicSignature);
}
