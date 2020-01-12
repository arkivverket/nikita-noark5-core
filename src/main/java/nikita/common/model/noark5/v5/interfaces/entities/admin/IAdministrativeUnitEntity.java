package nikita.common.model.noark5.v5.interfaces.entities.admin;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ICreate;
import nikita.common.model.noark5.v5.interfaces.entities.IFinalise;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tsodring on 1/16/17.
 */
public interface IAdministrativeUnitEntity
        extends ISystemId, ICreate, IFinalise, Serializable {

    String getShortName();

    void setShortName(String shortName);

    String getAdministrativeUnitName();

    void setAdministrativeUnitName(String administrativeUnitName);

    String getAdministrativeUnitStatus();

    void setAdministrativeUnitStatus(String administrativeUnitStatus);

    AdministrativeUnit getParentAdministrativeUnit();

    void setParentAdministrativeUnit(AdministrativeUnit referenceParentAdministrativeUnit);

    List<AdministrativeUnit> getReferenceChildAdministrativeUnit();

    void setReferenceChildAdministrativeUnit(List<AdministrativeUnit> referenceChildAdministrativeUnit);
}
