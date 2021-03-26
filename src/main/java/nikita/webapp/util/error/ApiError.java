package nikita.webapp.util.error;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.http.HttpStatus;

public class ApiError {

    private int status;
    private String message;
    private String developerMessage;
    private String stackTrace;

    public ApiError(final HttpStatus status, final String message,
		    final String developerMessage, final String stackTrace) {
        super();

        this.status = status.value();
        this.message = message;
        this.developerMessage = developerMessage;
        this.stackTrace = stackTrace;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(final String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(status)
                .append(message)
                .append(developerMessage)
                .append(stackTrace)
                .toHashCode();
    }

    @Override
    public boolean equals(Object otherEntity) {
        if (otherEntity == null) {
            return false;
        }
        if (otherEntity == this) {
            return true;
        }
        if (otherEntity.getClass() != getClass()) {
            return false;
        }
        return new EqualsBuilder()
                .appendSuper(super.equals(otherEntity))
                .append(this.status, ((ApiError) otherEntity).status)
                .append(this.message, ((ApiError) otherEntity).message)
                .append(this.developerMessage,
                        ((ApiError) otherEntity).developerMessage)
                .append(this.stackTrace, ((ApiError) otherEntity).stackTrace)
                .isEquals();
    }

    // ignore stackTrace here!
    @Override
    public final String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ApiError [status=").append(status)
                .append(", message=").append(message)
                .append(", developerMessage=").append(developerMessage)
                .append("]");
        return builder.toString();
    }
}
