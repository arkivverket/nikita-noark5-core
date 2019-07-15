package nikita.common.config;

import static nikita.common.config.Constants.*;

/**
 * Created by tsodring on 2/07/19.
 * <p>
 * USed to hold all constant values to DELETE manytomany join tables
 */
public final class DatabaseConstants {

    public static final String DELETE_FROM = "DELETE FROM ";
    public static final String INSERT_INTO = "INSERT INTO ";
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

    public static final String METADATA_INSERT_STRING =
            " (code, name, comment, system_id) VALUES ( :code, :name, " +
                    ":comment, :systemId )";

    // Used for reflection
    public static final String METADATA_REPOSITORY_PACKAGE =
            "nikita.common.repository.n5v5.metadata";

    public static final String METADATA_ENTITY_PACKAGE =
            "nikita.common.model.noark5.v5.metadata";
}
