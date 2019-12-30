package nikita.common.config;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;

/**
 * Created by tsodring on 3/22/17.
 */
public final class PATHPatterns {
    public static final String PATTERN_NEW_FONDS_STRUCTURE_ALL = SLASH + HREF_BASE_FONDS_STRUCTURE + SLASH + NEW + DASH + "*";
    public static final String PATTERN_METADATA_PATH = SLASH + HREF_BASE_METADATA + SLASH + "**";
    public static final String PATTERN_FONDS_STRUCTURE_FONDS = SLASH + HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH + FONDS + SLASH + "**";
    public static final String PATTERN_ADMIN_USER = SLASH + HREF_BASE_ADMIN + SLASH + USER + SLASH + "**";
    public static final String PATTERN_ADMIN_NEW_USER = SLASH + HREF_BASE_ADMIN + SLASH + NEW_USER + SLASH + "**";
}
