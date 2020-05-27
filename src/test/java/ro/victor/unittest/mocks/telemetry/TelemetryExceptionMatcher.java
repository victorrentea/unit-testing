package ro.victor.unittest.mocks.telemetry;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class TelemetryExceptionMatcher extends BaseMatcher<TelemetryException> {
    private final TelemetryException.ErrorCode expectedCode;

    TelemetryExceptionMatcher(TelemetryException.ErrorCode expectedCode) {
        this.expectedCode = expectedCode;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof TelemetryException)) {
            return false;
        }
        TelemetryException e = (TelemetryException) item;
        return e.getErrorCode() == expectedCode;
    }

    @Override
    public void describeTo(Description description) {

    }
}
