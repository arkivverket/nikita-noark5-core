package nikita.webapp.service.impl;

import nikita.common.model.noark5.v4.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v4.interfaces.entities.INoarkTitleDescriptionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

public class NoarkService {

    private static final Logger logger =
            LoggerFactory.getLogger(NoarkService.class);

    protected EntityManager entityManager;
    protected ApplicationEventPublisher applicationEventPublisher;

    public NoarkService(EntityManager entityManager,
                        ApplicationEventPublisher applicationEventPublisher) {
        this.entityManager = entityManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    protected void updateTitleAndDescription(
            @NotNull INoarkTitleDescriptionEntity incomingEntity,
            @NotNull INoarkTitleDescriptionEntity existingEntity) {
        if (null != incomingEntity.getTitle()) {
            existingEntity.setTitle(incomingEntity.getTitle());
        }
        existingEntity.setDescription(incomingEntity.getDescription());
    }

    protected void updateCodeAndDescription(
            @NotNull IMetadataEntity incomingEntity,
            @NotNull IMetadataEntity existingEntity) {
        // Copy all the values you are allowed to copy ....
        if (null != incomingEntity.getCode()) {
            existingEntity.setCode(incomingEntity.getCode());
        }
        existingEntity.setDescription(incomingEntity.getDescription());
    }

    protected String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    protected String getServletPath() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().toString();
    }

}
