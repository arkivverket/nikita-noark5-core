package nikita.webapp.service.impl.metadata;

import nikita.common.model.noark5.v5.interfaces.entities.INikitaEntity;
import nikita.common.model.noark5.v5.metadata.FondsStatus;
import nikita.common.repository.n5v5.metadata.IFondsStatusRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.service.impl.NoarkService;
import nikita.webapp.service.interfaces.metadata.IFondsStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

import static nikita.common.config.Constants.*;

@Service
@Transactional
public class FondsStatusService
        extends NoarkService
        implements IFondsStatusService {

    private static final Logger logger =
            LoggerFactory.getLogger(FondsStatusService.class);

    private IFondsStatusRepository fondsStatusRepository;

    public FondsStatusService(
            EntityManager entityManager,
            ApplicationEventPublisher applicationEventPublisher,
            IFondsStatusRepository fondsStatusRepository) {
        super(entityManager, applicationEventPublisher);
        this.fondsStatusRepository = fondsStatusRepository;
    }

    // All CREATE operations

    /**
     * Persists a new fondsStatus object to the database.
     *
     * @param fondsStatus fondsStatus object with values set
     * @return the newly persisted fondsStatus object
     */
    @Override
    public FondsStatus createNewFondsStatus(FondsStatus fondsStatus) {
        return fondsStatusRepository.save(fondsStatus);
    }

    // All READ operations

    /**
     * retrieve all fondsStatus
     *
     * @return
     */
    @Override
    public ArrayList<INikitaEntity> findAll() {
        return (ArrayList<INikitaEntity>)
                (ArrayList) fondsStatusRepository
                        .findAll();
    }


    /**
     * retrieve all fondsStatus that have a particular code. <br>
     * This will be replaced by OData search.
     *
     * @param code
     * @return
     */
    @Override
    public FondsStatus findByCode(String code) {
        return fondsStatusRepository.findByCode(code);
    }

    /**
     * retrieve a FondsStatus identified by particular code.  Raise
     * exception if the code is unknown.
     *
     * @param code The code of the object you wish to retrieve
     * @return The FondsStatus object wrapped
     */
    @Override
    public FondsStatus findFondsStatusByCode(String code) {
        return getFondsStatusOrThrow(code);
    }

    /**
     * update a particular fondsStatus. <br>
     *
     * @param fondsStatus
     * @return the updated fondsStatus
     */
    @Override
    public FondsStatus update(FondsStatus fondsStatus) {
        return fondsStatusRepository.save(fondsStatus);
    }

    /**
     * Internal helper method. Rather than having a find and try catch in
     * multiple methods, we have it here once. If you call this, be aware
     * that you will only ever get a valid FondsStatus object back. If there
     * is no FondsStatus object, a NoarkEntityNotFoundException exception
     * is thrown
     *
     * @param code The code of the FondsStatus object to retrieve
     * @return the FondsStatus object
     */
    private FondsStatus getFondsStatusOrThrow(@NotNull String code) {
        FondsStatus fondsStatus =
	    fondsStatusRepository.findByCode(code);
        if (fondsStatus == null) {
            String info = INFO_CANNOT_FIND_OBJECT +
		"FondsStatus, using " + "code " + code;
            logger.error(info);
            throw new NoarkEntityNotFoundException(info);
        }
        return fondsStatus;
    }
}
