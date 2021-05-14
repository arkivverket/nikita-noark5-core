package nikita.common.config;

public final class ErrorMessagesConstants {
    public static final String ERROR_MAPPING_METADATA =
            "During startup, nikita found a metadata ";
    public static final String METADATA_ENTITY_MISSING =
            "Cannot find metadata entity of type: ";
    public static final String METADATA_ENTITY_CLASS_MISSING =
            "Unable to find metadata entity class type: ";
    public static final String CROSS_REFERENCE_BAD_SYSTEM_ID = "The systemID " +
            "for fromSystemID [%s] in the CrossReference you tried to create " +
            "/ update does not equal the one [%s] present on the URL of the " +
            "HTTP request";
    public static final String CROSS_REFERENCE_DUPLICATE = "Attempt to " +
            "create duplicate cross reference from [%s] to [%s]";
}
