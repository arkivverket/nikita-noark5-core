package nikita.common.model.noark5.v5.interfaces.entities.secondary;

/**
 * Created by tsodring on 5/22/17.
 */
public interface ICorrespondencePartInternalEntity extends ICorrespondencePartEntity {

    String getAdministrativeUnit();

    void setAdministrativeUnit(String administrativeUnit);

//    AdministrativeUnit getReferenceAdministrativeUnit();

    ////  void setReferenceAdministrativeUnit(AdministrativeUnit
    //                                           referenceAdministrativeUnit);

    String getCaseHandler();

    void setCaseHandler(String caseHandler);

//    User getReferenceUser();

    //void setReferenceUser(User user);
/*
  TODO: Temp disabled!
    List<RegistryEntry> getReferenceRegistryEntry();

    void setReferenceRegistryEntry(List<RegistryEntry> referenceRegistryEntry);
*/
}
