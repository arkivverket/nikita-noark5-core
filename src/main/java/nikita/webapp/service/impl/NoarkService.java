package nikita.webapp.service.impl;

import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ITitleDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import static nikita.common.config.DatabaseConstants.ID;
import static nikita.common.util.CommonUtils.Validation.parseETAG;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestAsListOrThrow;
import static org.springframework.http.HttpHeaders.ALLOW;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpMethod.*;

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
            @NotNull ITitleDescription incomingEntity,
            @NotNull ITitleDescription existingEntity) {
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
        existingEntity.setCodeName(incomingEntity.getCodeName());
    }

    protected String getUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    protected String getServletPath() {
        return ((ServletRequestAttributes) RequestContextHolder.
                getRequestAttributes()).getRequest().getServletPath();
    }

    protected Long getETag() {
        return parseETAG(((ServletRequestAttributes) RequestContextHolder.
                getRequestAttributes()).getRequest().getHeader(ETAG));
    }

    protected boolean deletePossible(INoarkEntity entity) {
        // Note, you cannot delete an entity unless you the latest copy. The
        // following call may result in a NoarkConcurrencyException/409
        // Conflict
        entity.setVersion(getETag());

        if (entity.getOwnedBy().equals(getUser())) {
            return true;
        } else {
            String message = "User [" + getUser() + "] tried to delete a " +
                    entity.getBaseTypeName() + " with " +
                    entity.getIdentifierType() + "[" +
                    entity.getIdentifier() + "] but is not the owner";
            logger.error(message);
            throw new AccessDeniedException(message);
        }
    }

    /**
     * Set the outgoing OPTIONS header as well as the ETAG header
     *
     * @param noarkObject of type IHateoasNoarkObject
     */
    protected void setOutgoingRequestHeader(IHateoasNoarkObject noarkObject) {
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes()).getRequest();
        response.addHeader(ETAG,
                "\"" + noarkObject.getEntityVersion().toString() + "\"");
        response.addHeader(ALLOW, getMethodsForRequestAsListOrThrow(
                request.getServletPath()));
    }

    /**
     * Set the outgoing ALLOW header
     */
    protected void setOutgoingRequestHeaderList() {
        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        HttpServletRequest request =
                ((ServletRequestAttributes)
                        RequestContextHolder
                                .getRequestAttributes()).getRequest();
        response.addHeader(ALLOW, getMethodsForRequestAsListOrThrow(
                request.getServletPath()));
    }

    /**
     * See issue for a description of why this code was written this way
     * https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/82
     *
     * @param entity
     * @param deleteString
     */
    protected void disassociateForeignKeys(
            SystemIdEntity entity, String deleteString) {
        Query query = entityManager.createNativeQuery(deleteString);
        query.setParameter(ID, entity.getSystemId());
        query.executeUpdate();
    }

    protected void deleteEntity(NoarkEntity entity) {
        entityManager.remove(entity);
        entityManager.flush();
        entityManager.clear();
    }


    /**
     * Temporary fix until we figure out role based access control
     *
     * @return
     */
    protected HttpMethod[] getMethodDummy() {
        HttpMethod[] methods = new HttpMethod[5];
        methods[0] = GET;
        methods[1] = POST;
        methods[2] = DELETE;
        methods[3] = PUT;
        methods[4] = OPTIONS;
        return methods;
    }
}
