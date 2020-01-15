package nikita.webapp.util;

import nikita.common.model.noark5.v5.interfaces.IDocumentMedium;
import nikita.common.model.noark5.v5.interfaces.entities.IFinalise;
import nikita.common.model.noark5.v5.metadata.DocumentMedium;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.IDocumentMediumService;

import static nikita.common.config.N5ResourceMappings.*;

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
                    IDocumentMediumService service,
                    IDocumentMedium entity) {
                if (null != entity.getDocumentMediumCode()) {
                    DocumentMedium metadata =
                        (DocumentMedium) service.findValidMetadataOrThrow(
                                entity.getBaseTypeName(),
                                entity.getDocumentMediumCode(),
                                entity.getDocumentMediumCodeName());
                    entity.setDocumentMediumCodeName(metadata.getCodeName());
                }
            }
        }
    }
}
