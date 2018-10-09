package nikita.common.config;

/**
 * Exception details constants.
 *
 * Here, all descriptions to be used with an exception should be defined.
 */
public final class ExceptionDetailsConstants {


    public static final String MISSING_DOCUMENT_DESCRIPTION_ERROR =
            "You tried to create an archive version of a production document" +
                    "that has no parent document description.";


    private ExceptionDetailsConstants() {
    }
}
