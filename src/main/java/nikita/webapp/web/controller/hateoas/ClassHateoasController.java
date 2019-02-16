package nikita.webapp.web.controller.hateoas;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.model.noark5.v4.Class;
import nikita.common.model.noark5.v4.ClassificationSystem;
import nikita.common.model.noark5.v4.NoarkEntity;
import nikita.common.model.noark5.v4.hateoas.ClassHateoas;
import nikita.common.model.noark5.v4.hateoas.ClassificationSystemHateoas;
import nikita.common.model.noark5.v4.hateoas.HateoasNoarkObject;
import nikita.common.model.noark5.v4.interfaces.entities.INikitaEntity;
import nikita.common.util.exceptions.NikitaException;
import nikita.common.util.exceptions.NoarkEntityNotFoundException;
import nikita.webapp.hateoas.interfaces.IClassHateoasHandler;
import nikita.webapp.hateoas.interfaces.IClassificationSystemHateoasHandler;
import nikita.webapp.security.Authorisation;
import nikita.webapp.service.interfaces.IClassService;
import nikita.webapp.web.events.AfterNoarkEntityCreatedEvent;
import nikita.webapp.web.events.AfterNoarkEntityDeletedEvent;
import nikita.webapp.web.events.AfterNoarkEntityUpdatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.CLASS;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(
        value = HATEOAS_API_PATH + SLASH + NOARK_FONDS_STRUCTURE_PATH + SLASH +
                CLASS, produces = {NOARK5_V4_CONTENT_TYPE_JSON,
        NOARK5_V4_CONTENT_TYPE_JSON_XML})
public class ClassHateoasController
        extends NoarkController {

    private IClassService classService;
    private IClassHateoasHandler classHateoasHandler;
    private IClassificationSystemHateoasHandler
            classificationSystemHateoasHandler;
    private ApplicationEventPublisher applicationEventPublisher;

    public ClassHateoasController(
            IClassService classService,
            IClassHateoasHandler classHateoasHandler,
            IClassificationSystemHateoasHandler
                    classificationSystemHateoasHandler,
            ApplicationEventPublisher applicationEventPublisher) {
        this.classService = classService;
        this.classHateoasHandler = classHateoasHandler;
        this.classificationSystemHateoasHandler =
                classificationSystemHateoasHandler;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // API - All POST Requests (CRUD - CREATE)
    // POST [contextPath][api]/arkivstruktur/klasse/{systemID}/ny-underklasse
    @ApiOperation(value = "Persists a Class object associated with the " +
            "(other) given Class systemId", notes = "Returns the newly " +
            "created class object after it was associated with a class" +
            "object and persisted to the database",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Class " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Class.class),
            @ApiResponse(code = 201,
                    message = "Class " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = Class.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Class"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(method = POST,
            value = SLASH + LEFT_PARENTHESIS + "classificationSystemSystemId" +
                    RIGHT_PARENTHESIS + SLASH + NEW_SUB_CLASS,
            consumes = NOARK5_V4_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas>
    createClassAssociatedWithClassificationSystem(
            HttpServletRequest request,
            @ApiParam(name = "classificationSystemSystemId",
                    value = "systemId of classificationSystem to associate " +
                            "the klass with.",
                    required = true)
            @PathVariable String classSystemId,
            @ApiParam(name = "klass",
                    value = "Incoming class object",
                    required = true)
            @RequestBody Class klass)
            throws NikitaException {
        Class createdClass = classService.
                createClassAssociatedWithClass(classSystemId, klass);
        ClassHateoas classHateoas = new ClassHateoas(createdClass);
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        applicationEventPublisher
                .publishEvent(
                        new AfterNoarkEntityCreatedEvent(this, createdClass));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(createdClass.getVersion().toString())
                .body(classHateoas);
    }

    // API - All GET Requests (CRUD - READ)

    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID +
            RIGHT_PARENTHESIS,
            method = GET)
    public ResponseEntity<ClassHateoas> findOne(
            HttpServletRequest request,
            @ApiParam(name = "systemId",
                    value = "systemId of class to retrieve.",
                    required = true)
            @PathVariable("systemID") final String classSystemId) {
        Class klass = classService.findBySystemId(classSystemId);
        if (klass == null) {
            throw new NoarkEntityNotFoundException(classSystemId);
        }
        ClassHateoas classHateoas = new ClassHateoas(klass);
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .eTag(klass.getVersion().toString())
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classHateoas);
    }

    @ApiOperation(value = "Retrieves multiple Class entities limited by " +
            "ownership rights",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Class list found",
                    response = ClassHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted

    @RequestMapping(method = GET)
    public ResponseEntity<ClassHateoas> findAllClass(
            HttpServletRequest request) {
        ClassHateoas classHateoas = new
                ClassHateoas((List<INikitaEntity>) (List)
                classService.findAll());
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(classHateoas);
    }

    // Delete a Class identified by systemID
    // DELETE [contextPath][api]/arkivstruktur/klasse/{systemId}/
    @ApiOperation(value = "Deletes a single Class entity identified by systemID",
            response = HateoasNoarkObject.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Parent entity (ClassificationSystem or Class) " +
                            "returned",
                    response = HateoasNoarkObject.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID +
            RIGHT_PARENTHESIS,
            method = DELETE)
    public ResponseEntity<HateoasNoarkObject> deleteClassBySystemId(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemID of the class to delete",
                    required = true)
            @PathVariable("systemID") final String systemID) {

        Class klass = classService.findBySystemId(systemID);
        NoarkEntity parentEntity = klass.chooseParent();
        classService.deleteEntity(systemID);
        HateoasNoarkObject hateoasNoarkObject;
        if (parentEntity instanceof Class) {
            hateoasNoarkObject = new ClassHateoas(parentEntity);
            classHateoasHandler.addLinks(
                    hateoasNoarkObject, new Authorisation());
        }
        else if (parentEntity instanceof ClassificationSystem) {
            hateoasNoarkObject = new ClassificationSystemHateoas(parentEntity);
            classificationSystemHateoasHandler
                    .addLinks(hateoasNoarkObject, new Authorisation());
        }
        else {
            throw new NikitaException("Internal error. Could process" +
                    request.getRequestURI());
        }
        applicationEventPublisher
                .publishEvent
                        (new AfterNoarkEntityDeletedEvent(this, klass));
        return ResponseEntity.status(OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(hateoasNoarkObject);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a Class
    // PUT [contextPath][api]/arkivstruktur/klasse/{systemID}
    @ApiOperation(value = "Updates a Class object", notes = "Returns the " +
            "newly updated Class object after it is persisted to the database",
            response = ClassHateoas.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Class " + API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = ClassHateoas.class),
            @ApiResponse(code = 201,
                    message = "Class " + API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = ClassHateoas.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(code = 404,
                    message = API_MESSAGE_PARENT_DOES_NOT_EXIST +
                            " of type Class"),
            @ApiResponse(code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @RequestMapping(method = PUT,
            value = SLASH + LEFT_PARENTHESIS + SYSTEM_ID +
                    RIGHT_PARENTHESIS, consumes = NOARK5_V4_CONTENT_TYPE_JSON)
    public ResponseEntity<ClassHateoas> updateClass(
            HttpServletRequest request,
            @ApiParam(name = "systemID",
                    value = "systemId of class to update.",
                    required = true)
            @PathVariable("systemID") String systemID,
            @ApiParam(name = "class",
                    value = "Incoming class object",
                    required = true)
            @RequestBody Class klass)
            throws NikitaException {
        validateForUpdate(klass);
        Class updatedClass =
                classService.handleUpdate(systemID,
                        parseETAG(request.getHeader(ETAG)), klass);
        ClassHateoas classHateoas = new ClassHateoas(updatedClass);
        classHateoasHandler.addLinks(classHateoas, new Authorisation());
        applicationEventPublisher.
                publishEvent(
                        new AfterNoarkEntityUpdatedEvent(this, updatedClass));
        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(updatedClass.getVersion().toString())
                .body(classHateoas);
    }
}
