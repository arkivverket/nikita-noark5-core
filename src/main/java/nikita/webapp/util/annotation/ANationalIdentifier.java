package nikita.webapp.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to define that a entity class is a national identifier and return the
 * name of the national identifier e.g. a Unit uses enhetsidentifikator as
 * outer entity name
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ANationalIdentifier {
    String name();
}


