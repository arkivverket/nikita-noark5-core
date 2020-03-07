package nikita.webapp.util;

import nikita.common.model.noark5.v5.interfaces.IDeletion;
import nikita.common.model.noark5.v5.interfaces.IDocumentMedium;
import nikita.common.model.noark5.v5.interfaces.entities.IDeletionEntity;
import nikita.common.model.noark5.v5.interfaces.entities.IFinalise;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.model.noark5.v5.secondary.Deletion;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.service.interfaces.metadata.IMetadataService;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

import static nikita.common.config.N5ResourceMappings.DOCUMENT_MEDIUM;

public final class NoarkUtils {

    // You shall not instantiate me!
    private NoarkUtils() {
    }

    public static final class NoarkEntity {

        public static final class Create {
            public static void setFinaliseEntityValues(
                    IFinalise finaliseEntity) {
                finaliseEntity.setFinalisedDate(null);
                finaliseEntity.setFinalisedBy(null);
            }

            public static void validateDocumentMedium(
                    IMetadataService service,
                    IDocumentMedium entity) {
                if (null != entity.getDocumentMedium()) {
                    DocumentMedium metadata = (DocumentMedium)
                            service.findValidMetadataByEntityTypeOrThrow(
                                    DOCUMENT_MEDIUM,
                                    entity.getDocumentMedium());
                    entity.setDocumentMedium(metadata);
                }
            }
            public static void validateDeletion(IDeletionEntity entity) {
                if (null == entity)
                    return;
                if (0 > OffsetDateTime.now().compareTo(entity.getDeletionDate())) {
                    String info = "Deletion date is in the future.";
                    throw new NikitaMalformedInputDataException(info);
                }
            }
	    public static void updateDeletion(
		    @NotNull final IDeletion incoming,
		    @NotNull final IDeletion existing) {
		Deletion incomingDeletion = incoming.getReferenceDeletion();
		Deletion existingDeletion = existing.getReferenceDeletion();
		if (null == existingDeletion && null == incomingDeletion)
		    return;
		if (null != existingDeletion && null == incomingDeletion) {
		    existing.setReferenceDeletion(null);
		    return;
		}
		validateDeletion(incomingDeletion);
		existing.setReferenceDeletion(incoming.getReferenceDeletion());
	    }

        }
    }
}
