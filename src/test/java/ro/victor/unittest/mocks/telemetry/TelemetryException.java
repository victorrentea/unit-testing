package ro.victor.unittest.mocks.telemetry;

import org.springframework.validation.Errors;

public class TelemetryException extends RuntimeException {

    public enum ErrorCode {
        GENERAL,
        VALUE_IS_1,
        NOT_CONNECTED
    }

    private final ErrorCode errorCode;

    public TelemetryException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public TelemetryException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public TelemetryException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public TelemetryException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public TelemetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
