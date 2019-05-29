package nikita.webapp.util.annotation;

import nikita.common.model.noark5.v5.hateoas.HateoasNoarkObject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ANNOTATION_TYPE, TYPE})
@Retention(RUNTIME)
public @interface HateoasObject {
    Class<? extends HateoasNoarkObject> using()
            default HateoasNoarkObject.class;
}
