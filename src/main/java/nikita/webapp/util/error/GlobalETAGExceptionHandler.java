package nikita.webapp.util.error;

import nikita.common.util.exceptions.NikitaETAGMalformedHeaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
class GlobalETAGExceptionHandler {
    private final Logger logger =
            LoggerFactory.getLogger(GlobalETAGExceptionHandler.class);

    /**
     * The point of this class is to capture exceptions that occur outside a
     * controller.This is related to
     * https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/76
     * <p>
     * Also some antlr exceptions appear to go outside the
     * ResponseEntityExceptionHandler approach, but this might be because of
     * forwarding
     **/
    @ExceptionHandler(value = {NikitaETAGMalformedHeaderException.class,
            Exception.class})
    public ResponseEntity<ApiError> defaultErrorHandler(
            HttpServletRequest request, Exception ex) {
        final String message = ex.getMessage() == null ?
                ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = getRootCauseMessage(ex);
        final String devStackTrace = ex.toString();
        logger.error(message);
        ApiError apiError = new ApiError(
                BAD_REQUEST, message.replace("\"", "\\\""),
                devMessage.replace("\"", "\\\""),
                devStackTrace.replace("\"", "\\\""));
        return ResponseEntity.status(BAD_REQUEST)
                .body(apiError);
    }
}
