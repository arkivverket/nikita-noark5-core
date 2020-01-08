package nikita.webapp.util;

import nikita.common.model.noark5.v5.interfaces.IDocumentMedium;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkFinaliseEntity;
import nikita.common.util.exceptions.NikitaException;

import static nikita.common.config.N5ResourceMappings.*;

public final class NoarkUtils {

    // FIXME look up valid values in codelist, do not hardcode here.
    private final static String[] documentMedium = {
            DOCUMENT_MEDIUM_ELECTRONIC, DOCUMENT_MEDIUM_PHYSICAL,
            DOCUMENT_MEDIUM_MIXED};

    // You shall not instantiate me!
    private NoarkUtils() {
    }

    public static final class NoarkEntity {

        public static final class Create {
            public static void setFinaliseEntityValues(
                    INoarkFinaliseEntity finaliseEntity) {
                finaliseEntity.setFinalisedDate(null);
                finaliseEntity.setFinalisedBy(null);
            }

            public static boolean checkDocumentMediumValid(
                    IDocumentMedium documentMediumEntity) {
                // Assume there will not be a valid value
                boolean invalidValueFound = false;

                if (documentMediumEntity != null &&
                        documentMediumEntity.getDocumentMediumCodeName() != null) {
                    for (String s : documentMedium) {
                        // if a valid value is found, then all is OK
                        if (s.equals(
                                documentMediumEntity.getDocumentMediumCodeName())) {
                            invalidValueFound = true;
                        }
                    }
                }
                // null is also a valid value
                else {
                    invalidValueFound = true;
                }
                // No valid value for documentMedium was found this is a problem
                // throw an exception back to the caller
                if (!invalidValueFound) {
                    throw new NikitaException("Document medium not a valid " +
                            "document medium. Object causing this is " +
                            documentMediumEntity.toString());
                }
                return invalidValueFound;
            }
        }
    }
}
