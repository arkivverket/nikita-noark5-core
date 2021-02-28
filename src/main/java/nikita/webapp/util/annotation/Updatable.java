package nikita.webapp.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used on columns in domain class defintions to define whether or not a column
 * can be changed with a PATCH
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Updatable {
}


