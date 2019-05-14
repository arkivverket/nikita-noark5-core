package nikita.webapp.util.annotation;

import nikita.webapp.hateoas.HateoasHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
public @interface HateoasPacker {
    Class<? extends HateoasHandler> using()
            default HateoasHandler.class;
}
