package nikita.webapp.service.application;

import nikita.common.model.nikita.PatchObject;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.repository.n5v5.ISystemIdEntityRepository;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.fromString;

@Service
@Transactional
public class PatchService
        implements IPatchService {

    private static final Logger logger =
            LoggerFactory.getLogger(PatchService.class);

    protected final ISystemIdEntityRepository systemIdEntityRepository;

    public PatchService(ISystemIdEntityRepository systemIdEntityRepository) {
        this.systemIdEntityRepository = systemIdEntityRepository;
    }

    public void handlePatch(UUID originalObjectId,
                            PatchObject patchObject) {
        SystemIdEntity what = findSystemIdEntity(originalObjectId);
        SystemIdEntity fromObject = findSystemIdEntity(getFrom(patchObject));
        SystemIdEntity toObject = findSystemIdEntity(getTo(patchObject));
        System.out.println("hello");
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
