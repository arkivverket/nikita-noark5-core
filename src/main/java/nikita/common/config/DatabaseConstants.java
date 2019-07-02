package nikita.common.config;

import static nikita.common.config.Constants.*;

/**
 * Created by tsodring on 2/07/19.
 * <p>
 * USed to hold all constant values to DELETE manytomany join tables
 */
public final class DatabaseConstants {

    public static final String DELETE_FROM = "DELETE FROM ";
    public static final String ID = "id";
    public static final String WHERE = " WHERE ";
    public static final String EQUALS_ID = "  = :id ;";

    public static final String DELETE_FROM_FONDS_CREATOR_FONDS =
            DELETE_FROM + TABLE_FONDS_FONDS_CREATOR + WHERE +
                    FOREIGN_KEY_FONDS_CREATOR_PK + EQUALS_ID;

    public static final String DELETE_FONDS_STORAGE_LOCATION =
            DELETE_FROM + TABLE_FONDS_STORAGE_LOCATION + WHERE +
                    FOREIGN_KEY_FONDS_PK + EQUALS_ID;

    public static final String DELETE_FROM_FONDS_FONDS_CREATOR =
            DELETE_FROM + TABLE_FONDS_FONDS_CREATOR + WHERE +
                    FOREIGN_KEY_FONDS_PK + EQUALS_ID;

}
