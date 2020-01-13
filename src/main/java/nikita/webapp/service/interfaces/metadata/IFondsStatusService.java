package nikita.webapp.service.interfaces.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.FondsStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsodring on 3/9/17.
 */
public interface IFondsStatusService {

    FondsStatus createNewFondsStatus(FondsStatus fondsStatus);

    List <IMetadataEntity> findAll();

    FondsStatus findByCode(String code);

    FondsStatus findFondsStatusByCode(String code);

    FondsStatus update(FondsStatus fondsStatus);
}
