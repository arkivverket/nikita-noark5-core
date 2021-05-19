package nikita.webapp.util;

import nikita.webapp.util.annotation.ANationalIdentifier;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

import static nikita.common.util.CommonUtils.FileUtils.addClassToMap;
import static nikita.common.util.CommonUtils.FileUtils.addClassToNatIdentMap;

@Component
public class DomainModelMapper {
    public void mapDomainModelClasses() {
        Reflections ref = new Reflections("nikita.common.model.noark5");
        for (Class<?> klass : ref.getTypesAnnotatedWith(Entity.class)) {
            String simpleName = klass.getSimpleName();
            addClassToMap(simpleName, klass);
            if (klass.isAnnotationPresent(ANationalIdentifier.class)) {
                addClassToNatIdentMap(simpleName, klass,
                        klass.getAnnotation(ANationalIdentifier.class));
            }
        }
    }
}
