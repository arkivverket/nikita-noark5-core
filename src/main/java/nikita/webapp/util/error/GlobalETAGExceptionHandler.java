package nikita.webapp.util.error;

import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaETAGMalformedHeaderException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by tsodring on 5/11/17.
 */
@ControllerAdvice
class GlobalETAGExceptionHandler {

    /**
     * The point of this class is to capture exceptions that occur outside a controller.
     * <p>
     * This is related to https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues/76
     **/

    @ExceptionHandler(value = NikitaETAGMalformedHeaderException.class)
    public ResponseEntity<String> defaultErrorHandler(HttpServletRequest request, Exception ex) {
        final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        final String devMessage = ExceptionUtils.getRootCauseMessage(ex);
        final String devStackTrace = ex.toString();

        ApiError apiError = new ApiError(BAD_REQUEST, message, devMessage, devStackTrace);
        return ResponseEntity.status(BAD_REQUEST)
                .allow(CommonUtils.WebUtils.getMethodsForRequestOrThrow(request.getServletPath()))
                .body(apiError.toJSON());
    }
}
