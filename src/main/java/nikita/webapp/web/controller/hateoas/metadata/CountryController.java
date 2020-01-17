package nikita.webapp.web.controller.hateoas.metadata;

import com.codahale.metrics.annotation.Counted;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nikita.common.config.Constants;
import nikita.common.model.noark5.v5.hateoas.metadata.MetadataHateoas;
import nikita.common.model.noark5.v5.metadata.Country;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaException;
import nikita.webapp.service.interfaces.metadata.ICountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static nikita.common.config.Constants.*;
import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Validation.parseETAG;
import static nikita.common.util.CommonUtils.WebUtils.getMethodsForRequestOrThrow;
import static org.springframework.http.HttpHeaders.ETAG;

/**
 * Created by tsodring on 14/03/18.
 */

@RestController
@RequestMapping(value = HREF_BASE_METADATA + SLASH,
                produces = NOARK5_V5_CONTENT_TYPE_JSON)
@SuppressWarnings("unchecked")
public class CountryController {

    private ICountryService countryService;

    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    // API - All POST Requests (CRUD - CREATE)
    // Creates a new land
    // POST [contextPath][api]/metadata/ny-land
    @ApiOperation(
            value = "Persists a new Country object",
            notes = "Returns the newly created Country object after it " +
                    "is persisted to the database",
            response = Country.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Country " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Country.class),
            @ApiResponse(
                    code = 201,
                    message = "Country " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = Country.class),
            @ApiResponse(code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PostMapping(value = NEW_COUNTRY)
    public ResponseEntity<MetadataHateoas> createCountry(
            HttpServletRequest request,
            @RequestBody Country country)
            throws NikitaException {

        MetadataHateoas metadataHateoas =
                countryService.createNewCountry(country);

        return ResponseEntity.status(HttpStatus.CREATED)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // API - All GET Requests (CRUD - READ)
    // Retrieves all country
    // GET [contextPath][api]/metadata/land/
    @ApiOperation(
            value = "Retrieves all Country ",
            response = Country.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Country codes found",
                    response = Country.class),
            @ApiResponse(
                    code = 404,
                    message = "No Country found"),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = COUNTRY)
    public ResponseEntity<MetadataHateoas> findAll(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(countryService.findAll());
    }

    // Retrieves a given country identified by a code
    // GET [contextPath][api]/metadata/land/{kode}/
    @ApiOperation(
            value = "Gets country identified by its code",
            notes = "Returns the requested country object",
            response = Country.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Country " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Country.class),
            @ApiResponse(
                    code = 201,
                    message = "Country " +
                            API_MESSAGE_OBJECT_SUCCESSFULLY_CREATED,
                    response = Country.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = COUNTRY + SLASH + CODE_PARAMETER + SLASH)
    public ResponseEntity<MetadataHateoas> findByCode(
            @PathVariable("kode") final String code,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = countryService.findByCode(code);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .eTag(metadataHateoas.getEntityVersion().toString())
                .body(metadataHateoas);
    }

    // Create a suggested country(like a template) with default values
    // (nothing persisted)
    // GET [contextPath][api]/metadata/ny-land
    @ApiOperation(
            value = "Creates a suggested Country",
            response = Country.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Country codes found",
                    response = Country.class),
            @ApiResponse(
                    code = 404,
                    message = "No Country found"),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @GetMapping(value = NEW_COUNTRY)
    public ResponseEntity<MetadataHateoas>
    generateDefaultCountry(HttpServletRequest request) {

        MetadataHateoas metadataHateoas = new MetadataHateoas
                (countryService.generateDefaultCountry());

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }

    // API - All PUT Requests (CRUD - UPDATE)
    // Update a land
    // PUT [contextPath][api]/metadata/land/{kode}
    @ApiOperation(
            value = "Updates a Country object",
            notes = "Returns the newly updated Country object after it " +
                    "is persisted to the database",
            response = Country.class)
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Country " +
                            API_MESSAGE_OBJECT_ALREADY_PERSISTED,
                    response = Country.class),
            @ApiResponse(
                    code = 401,
                    message = API_MESSAGE_UNAUTHENTICATED_USER),
            @ApiResponse(
                    code = 403,
                    message = API_MESSAGE_UNAUTHORISED_FOR_USER),
            @ApiResponse(
                    code = 404,
                    message = API_MESSAGE_MALFORMED_PAYLOAD),
            @ApiResponse(
                    code = 409,
                    message = API_MESSAGE_CONFLICT),
            @ApiResponse(
                    code = 500,
                    message = API_MESSAGE_INTERNAL_SERVER_ERROR)})
    @Counted
    @PutMapping(value = COUNTRY + SLASH + CODE_PARAMETER)
    public ResponseEntity<MetadataHateoas> updateCountry(
            @ApiParam(name = CODE,
                    value = "code of fonds to update.",
                    required = true)
            @PathVariable(CODE) String code,
            @RequestBody Country country,
            HttpServletRequest request) {

        MetadataHateoas metadataHateoas = countryService.handleUpdate
                (code, parseETAG(request.getHeader(ETAG)), country);

        return ResponseEntity.status(HttpStatus.OK)
                .allow(getMethodsForRequestOrThrow(request.getServletPath()))
                .body(metadataHateoas);
    }
}
