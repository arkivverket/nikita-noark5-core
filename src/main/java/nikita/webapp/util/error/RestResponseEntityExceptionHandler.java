package nikita.webapp.util.error;


import nikita.common.util.exceptions.*;
import nikita.webapp.util.exceptions.UsernameExistsException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This is an implementation of a global exception handler extending the
 * ResponseEntityExceptionHandler in order to gain control status codes and
 * response bodies that will be sent to the client. This allows us to give
 * more useful information back to the client
 *
 * Originally adapted from baeldung, similar description can be found here:
 *   https://www.baeldung.com/exception-handling-for-rest-with-spring
 */

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    public RestResponseEntityExceptionHandler() {
        super();
    }

    // 4XX range of status codes
    // 400 Bad Requests

    // If there is a problem with
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex, final HttpHeaders headers,
            final HttpStatus status, final WebRequest request) {
        return handleExceptionInternal(ex, message(status, ex),
                headers, HttpStatus.BAD_REQUEST, request);
    }

    // If there is a problem with incoming arguments i.e null value where
    // value is required
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders
            headers, final HttpStatus status, final WebRequest request) {
        return handleExceptionInternal(ex, message(status, ex), headers,
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(
            final DataIntegrityViolationException ex,
            final WebRequest request) {
        return handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 403
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            final Exception ex,
            final WebRequest request) {
        return new ResponseEntity<Object>("Feil under pålogging",
                new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    // 404
    @ExceptionHandler(value = {NoarkEntityNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(
            final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, message(HttpStatus.NOT_FOUND, ex),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // 406
    @ExceptionHandler(value = { NoarkNotAcceptableException.class })
    protected ResponseEntity<Object> handleNotAcceptable(
            final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, message(HttpStatus.NOT_ACCEPTABLE,
                ex), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(value = {UsernameExistsException.class})
    protected ResponseEntity<Object> handleUserExists(
            final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, message(HttpStatus.NOT_ACCEPTABLE,
                ex), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(value = {NikitaMalformedInputDataException.class})
    protected ResponseEntity<Object> handleMalformedDataInput(
            final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 500
    @ExceptionHandler({NullPointerException.class,
            IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleInternal(
            final RuntimeException ex,
            final WebRequest request) {
        logger.error("500 Status Code", ex);
        final String bodyOfResponse = "En feil skjedde i nikita-serveren!";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Object> handleStorageException(
            final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        logger.error(request.getDescription(true), ex);
        return handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NikitaMalformedHeaderException.class)
    public ResponseEntity<Object> handleMalformedHeaderException(
            final RuntimeException ex, final WebRequest request) {
        logger.error("400 Status Code", ex);
        logger.error(request.getDescription(true), ex);
        return handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private ApiError message(final HttpStatus httpStatus, final Exception ex) {
        logger.error("REST Exception occurred " + ex.getMessage());
        final String message = ex.getMessage() == null ?
                ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = ExceptionUtils.getRootCauseMessage(ex);
        final String devStackTrace = ex.toString();

        return new ApiError(
                httpStatus.value(), message, devMessage, devStackTrace);
    }
}
