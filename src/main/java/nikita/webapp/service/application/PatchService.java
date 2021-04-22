package nikita.webapp.service.application;

import nikita.common.model.nikita.PatchObject;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.repository.n5v5.ISystemIdEntityRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.common.util.exceptions.PatchMisconfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.fromString;
import static nikita.common.util.CommonUtils.WebUtils.getEnglishNameObject;

@Service
public class PatchService
        implements IPatchService {

    private static final Logger logger =
            LoggerFactory.getLogger(PatchService.class);

    protected final ISystemIdEntityRepository systemIdEntityRepository;

    public PatchService(ISystemIdEntityRepository systemIdEntityRepository) {
        this.systemIdEntityRepository = systemIdEntityRepository;
    }

    @Transactional
    public Object handlePatch(UUID originalObjectId,
                              PatchObject patchObject) {
        SystemIdEntity what = findSystemIdEntity(originalObjectId);
        SystemIdEntity fromObject = findSystemIdEntity(getFrom(patchObject));
        SystemIdEntity toObject = findSystemIdEntity(getTo(patchObject));

        try {

            // First update reference between what and toObject
            // e.g. Move File (what) from Series (fromObject) to Series
            // (toObject)
            // We need to call File::setReferenceSeries(what)
            String toObjectName = toObject.getClass().getSimpleName();
            // Some classes may be proxied and will have a name like
            // Series$HibernateProxy$xsdrs. This does not allow us to re-use
            // the name properly. This is a hint that perhaps we should use a
            // different approach than picking out names. Perhaps use
            // annotations in some way.
            if (toObjectName.contains("$HibernateProxy")) {
                toObjectName = toObjectName.split("\\$HibernateProxy")[0];
            }

            String toMethodName = "setReference" + getEnglishNameObject(
                    toObjectName);
            Method updateWhat = what.getClass().getMethod(toMethodName,
                    toObject.getClass());
            updateWhat.invoke(what, toObject);

            // Then remove the reference from fromObject tp what
            // e.g. Move File (what) from Series (fromObject) to Series
            // (toObject)
            // We need to call Series(toObject)::removeFile(what)
            // Note: Remove *must* happen before add as the domain model
            // will set the other end of the reference to null with a remove
            String removeMethodName = "remove" + getEnglishNameObject(
                    what.getClass().getSimpleName());
            Method removeFrom = fromObject.getClass()
                    .getMethod(removeMethodName, what.getClass());
            removeFrom.invoke(fromObject, what);

            // Next set reference between toObject and what
            // e.g. Move File (what) from Series (fromObject) to Series
            // (toObject)
            // We need to call Series(fromObject)::addFile(what)
            String addMethodName = "add" + getEnglishNameObject(
                    what.getClass().getSimpleName());
            Method addTo = toObject.getClass().getMethod(addMethodName,
                    what.getClass());
            addTo.invoke(toObject, what);

            logger.info("Moved " + what.getClass().getSimpleName() +
                    " (" + what.getSystemIdAsString() + ") from " +
                    fromObject.getClass().getSimpleName() +
                    " (" + fromObject.getSystemIdAsString() + ") to " +
                    toObject.getClass().getSimpleName() +
                    " (" + toObject.getSystemIdAsString() + ")");
        } catch (SecurityException | NoSuchMethodException |
                IllegalArgumentException | IllegalAccessException |
                InvocationTargetException e) {
            String error = "Cannot find internal method from Patch : " +
                    what.getClass().getName() + " " +
                    " : " + e.getMessage();
            logger.error(error);
            throw new PatchMisconfigurationException(error);
        }
        return what;
    }

    private UUID getFrom(PatchObject patchObject) {
        return fromString(patchObject.getFrom());
    }

    private UUID getTo(PatchObject patchObject) {
        String value = patchObject.getPath();
        return fromString(value);
    }

    private SystemIdEntity findSystemIdEntity(UUID originalObjectId) {
        Optional<SystemIdEntity> systemIdEntity =
                systemIdEntityRepository.findById(originalObjectId);
        if (systemIdEntity.isPresent()) {
            return systemIdEntity.get();
        } else {
            String error = "Could not find Noark object with systemID " +
                    originalObjectId.toString();
            logger.error(error);
            throw new NoarkEntityNotFoundException(error);
        }
    }
}
