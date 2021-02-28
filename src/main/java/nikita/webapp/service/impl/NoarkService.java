package nikita.webapp.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import nikita.common.model.nikita.PatchMerge;
import nikita.common.model.nikita.PatchObject;
import nikita.common.model.nikita.PatchObjects;
import nikita.common.model.noark5.bsm.BSMBase;
import nikita.common.model.noark5.v5.NoarkEntity;
import nikita.common.model.noark5.v5.SystemIdEntity;
import nikita.common.model.noark5.v5.hateoas.IHateoasNoarkObject;
import nikita.common.model.noark5.v5.interfaces.entities.IMetadataEntity;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ITitleDescription;
import nikita.common.model.noark5.v5.md_other.BSMMetadata;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.common.util.exceptions.NikitaMisconfigurationException;
import nikita.common.util.exceptions.NoarkConcurrencyException;
import nikita.common.util.exceptions.PatchMisconfigurationException;
import nikita.webapp.service.interfaces.INoarkService;
import nikita.webapp.util.annotation.Updatable;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Version;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static nikita.common.config.DatabaseConstants.ID;
import static nikita.common.config.N5ResourceMappings.BSM_DEF;
import static nikita.common.config.N5ResourceMappings.PASSWORD_ENG;
import static nikita.common.util.CommonUtils.DATE_PATTERN;
import static nikita.common.util.CommonUtils.DATE_TIME_PATTERN;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDate;
import static nikita.common.util.CommonUtils.Hateoas.Deserialize.deserializeDateTime;
import static nikita.common.util.CommonUtils.Validation.parseETAG;
import static nikita.common.util.CommonUtils.WebUtils.getEnglishNameObjectOrThrow;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestAsListOrThrow;
import static org.springframework.http.HttpHeaders.ALLOW;
import static org.springframework.http.HttpHeaders.ETAG;

public class NoarkService
        implements INoarkService {

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
        return ((ServletRequestAttributes)
                Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()))
                .getRequest().getServletPath();
    }

    protected Long getETag() {
        return parseETAG(((ServletRequestAttributes)
                Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader(ETAG));
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
                Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()))
                .getResponse();
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes()).getRequest();
        Objects.requireNonNull(response);
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
                Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes()))
                .getResponse();
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
     * @param entity       The entity to disassociate foreign keys with
     * @param deleteString the delete SQL
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
     * An implementation of part of the PATCH (RFC 6902) standard
     * <p>
     * [
     * { "op": "replace", "path": "/requirementText", "value": "hello"},
     * ]
     * <p>
     * Swallows and converts the following exceptions to a
     * PatchMisconfigurationException:
     * <p>
     * - SecurityException if denied
     * - NoSuchMethodException method does not exist
     * - IllegalArgumentException problem with argument
     * - IllegalAccessException problem with access
     * - InvocationTargetException if it can't handle the syntax for some reason
     *
     * @param object       The object to update
     * @param patchObjects Multiple pathObject commands in one object
     *                     (contains an array)
     * @return The object after it was persisted
     * @throws PatchMisconfigurationException |
     */
    protected Object handlePatch(Object object, PatchObjects patchObjects) {
        for (PatchObject patchObject : patchObjects.getPatchObjects()) {
            if ("replace" .equalsIgnoreCase(patchObject.getOp())
                    // Make sure that path contains a column to change
                    && null != patchObject.getPath()
                    // and that there is at least one letter after the slash
                    && patchObject.getPath().length() > 1) {
                handlePatchReplace(object, patchObject);
            } else if ("add" .equalsIgnoreCase(patchObject.getOp())) {
                return handlePatchAdd((UUID) object, patchObject);
            } else if ("move" .equalsIgnoreCase(patchObject.getOp())) {
                handlePatchMove(object, patchObject);
            } else {
                String error = "Cannot handle this PatchObject : " +
                        patchObject.toString();
                logger.error(error);
                throw new PatchMisconfigurationException(error);
            }
        }
        return null;
    }

    protected void handlePatchMove(Object object, PatchObject patchObject) {

    }

    /**
     * {
     * "op": "add",
     * "path": ".../mappe/systemID",
     * "value": {
     * "virksomhetsspesifikkeMetadata": {
     * "ppt-v1:sakferdig": true,
     * "ppt-v1:datohenvist": "2020-06-30+02:00",
     * "ppt-v1:datotidvedtakferdig": "2020-06-30T15:35:43.128158+02:00",
     * "ppt-v1:skolekontakt": "Test BSM String",
     * "ppt-v1:refSkole": "https://skole.eksempel.com",
     * "ppt-v1:snittKarakter": 1.2,
     * "ppt-v1:antallDagerVurdert": 1
     * }
     * }
     * }
     *
     * @param uuid        Identity of object to add BSM
     * @param patchObject A single instance of a patchObject to apply
     */
    protected Object handlePatchAdd(UUID uuid, PatchObject patchObject) {
        List<BSMBase> bsm = getBSM(patchObject.getValue());
        checkBSM(bsm);
        return associateBSM(uuid, bsm);
    }

    protected void handlePatchReplace(Object object, PatchObject patchObject) {
        String path = patchObject.getPath();
        if ("/" .equals(path.substring(0, 1))) {
            path = path.substring(1);
        }
        updateObject(object, patchObject.getValue(), path);
    }

    protected void handlePatchMerge(Object object, PatchMerge patchMerge) {
        for (Map.Entry<String, Object> entry : patchMerge.getMap().entrySet()) {
            JsonNode valueNode = (JsonNode) entry.getValue();
            if (valueNode != null) {
                if (valueNode.isBoolean()) {
                    updateObject(object, valueNode.booleanValue(),
                            entry.getKey());
                } else if (valueNode.isDouble()) {
                    updateObject(object, valueNode.doubleValue(),
                            entry.getKey());
                } else if (valueNode.isInt()) {
                    updateObject(object, valueNode.intValue(), entry.getKey());
                } else if (valueNode.isTextual()) {
                    String value = valueNode.textValue();
                    if (DATE_TIME_PATTERN.matcher(value).matches()) {
                        updateObject(object,
                                deserializeDateTime(value),
                                entry.getKey());
                    } else if (DATE_PATTERN.matcher(value).matches()) {
                        updateObject(object,
                                deserializeDate(value),
                                entry.getKey());
                    } else {
                        updateObject(object, value, entry.getKey());
                    }
                }
            }
        }
    }

    protected void updateObject(Object object, Object value,
                                String incomingColumnName) {
        String column = getEnglishNameObjectOrThrow(incomingColumnName);
        String baseMethodName = column.substring(0, 1)
                .toUpperCase() + column.substring(1);
        String setMethodName = "set" + baseMethodName;
        String getMethodName = "get" + baseMethodName;
        try {
            Method getMethod =
                    object.getClass().getMethod(getMethodName);
            Class<T> retType = (Class<T>) getMethod.getReturnType();
            Method setMethod =
                    object.getClass().getMethod(setMethodName, retType);
            Method versionMethod =
                    object.getClass().getMethod("setVersion",
                            Long.class);
            // If the variable (path) you are trying to update is a
            // password then you have to encode the new password
            if (PASSWORD_ENG.equalsIgnoreCase(column)) {
                // Not possible to set password to null
                if (null != value) {
                    // Need to call method implemented in subclass userservice
                    //setMethod.invoke(object,
                    //        encoder.encode(value.toString()));
                }
            } else {
                versionMethod.invoke(object, getETag());
                if (null != value) {
                    setMethod.invoke(object, value);
                } else {
                    // Invoke the method with a null value
                    setMethod.invoke(object, new Object[]{null});
                }

            }
        } catch (SecurityException | NoSuchMethodException |
                IllegalArgumentException | IllegalAccessException |
                InvocationTargetException e) {
            // Avoid concurrency exception from being swallowed as an
            // InvocationTargetException
            if (e.getCause() instanceof NoarkConcurrencyException) {
                throw (NoarkConcurrencyException) e.getCause();
            }
            String error = "Cannot find internal method from Patch : " +
                    object.getClass().getName() + " " + incomingColumnName +
                    " : " + e.getMessage();
            logger.error(error);
            throw new PatchMisconfigurationException(error);
        }
    }

    private List<BSMBase> getBSM(Object object) {
        List<BSMBase> bsmList = new ArrayList<>();
        if (object instanceof LinkedHashMap) {
            LinkedHashMap values = (LinkedHashMap) ((LinkedHashMap) object)
                    .get(BSM_DEF);
            if (null != values) {
                for (Map.Entry<String, Object> entry :
                        (Iterable<Map.Entry<String, Object>>) values.entrySet()) {
                    if (entry.getValue() instanceof Boolean) {
                        bsmList.add(new BSMBase(entry.getKey(),
                                (Boolean) entry.getValue()));
                    } else if (entry.getValue() instanceof Double) {
                        bsmList.add(new BSMBase(entry.getKey(),
                                (Double) entry.getValue()));
                    } else if (entry.getValue() instanceof Integer) {
                        bsmList.add(new BSMBase(entry.getKey(),
                                (Integer) entry.getValue()));
                    } else if (entry.getValue() instanceof String) {
                        String value = (String) entry.getValue();
                        if (value.startsWith("http://") ||
                                value.startsWith("https://")) {
                            BSMBase bsmBase = new BSMBase(entry.getKey());
                            bsmBase.setUriValue(value);
                            bsmList.add(bsmBase);
                        } else if (DATE_TIME_PATTERN
                                .matcher(value).matches()) {
                            bsmList.add(new BSMBase(
                                    entry.getKey(),
                                    deserializeDateTime(value)));
                        } else if (DATE_PATTERN.matcher(value).matches()) {
                            bsmList.add(new BSMBase(entry.getKey(),
                                    deserializeDate(value), true));
                        } else {
                            bsmList.add(new BSMBase(entry.getKey(), value));
                        }
                    }
                }
            }
        }
        return bsmList;
    }

    protected Object associateBSM(@NotNull UUID systemID, List<BSMBase> bsm) {
        String error = "Cannot find internal nikita processor of BSM for " +
                "object with systemID " + systemID.toString();
        logger.error(error);
        throw new PatchMisconfigurationException(error);
    }

    /**
     * Update a systemID object using a RFC 7396 approach
     *
     * @param systemID   The systemID of the object to update
     * @param patchMerge Values to change
     * @return The updated BSMMetadata object wrapped as a BSMMetadataHateoas
     */
    // Note the sub-class has to implement this as the sub-class is aware of
    // its type
    public Object handleUpdateRfc7396(@NotNull UUID systemID,
                                      @NotNull PatchMerge patchMerge) {
        return null;
    }

    /**
     * @param object The object you want to update
     * @param column The column you want to update
     * @return The field if it is updatable, throw exception otherwise
     */
    private Field getUpdatableFieldOrThrow(Object object, String column) {
        try {
            Field field = object.getClass().getDeclaredField(column);
            if (null != field.getAnnotation(Updatable.class)) {
                return field;
            }
            String error = column + " is not an updatable attribute " +
                    "of class " + object.getClass().getName();
            logger.error(error);
            throw new NikitaMalformedInputDataException(error);
        } catch (NoSuchFieldException e) {
            String error = column + " is not an attribute associated with " +
                    object.getClass().getName();
            logger.error(error);
            throw new NikitaMalformedInputDataException(error);
        }
    }

    private Field getVersionFieldOrThrow(Object object) {
        Field[] fields = FieldUtils.getFieldsWithAnnotation(
                object.getClass(), Version.class);
        if (fields != null && fields.length == 1) {
            return fields[0];
        } else {
            String error = "Missing version attribute associated with " +
                    object.getClass().getName();
            logger.error(error);
            throw new NikitaMalformedInputDataException(error);
        }
    }

    /**
     * Used to retrieve a BSMBase object by getting the sub-class to
     * implement the repo class and return a BSMMetadata object if such an
     * object exists and is not outdated. Done to simplify coding.
     *
     * @param name Name of the BSM parameter to check
     * @return BSMMetadata object corresponding to the name
     */
    protected Optional<BSMMetadata> getBSMByName(String name) {
        String error = name + " unable to find subclass to process";
        logger.error(error);
        throw new NikitaMisconfigurationException(error);
    }

    private void checkBSM(List<BSMBase> bsm) {
        for (BSMBase bsmBase : bsm) {
            Optional<BSMMetadata> bsmMetadataOpt =
                    getBSMByName(bsmBase.getValueName());
            if (bsmMetadataOpt.isEmpty()) {
                String error = bsmBase.getValueName() + " is not a registered" +
                        " BSM datatype";
                logger.error(error);
                throw new PatchMisconfigurationException(error);
            } else if (bsmMetadataOpt.get().getOutdated()) {
                String error = bsmBase.getValueName() + " is outdated and " +
                        "cannot be assigned to any objects anymore";
                logger.error(error);
                throw new PatchMisconfigurationException(error);
            }
        }
    }

}
