package ro.victor.unittest.mocks.telemetry;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class SGExceptionMatcher extends BaseMatcher<SGException> {
    private final SGException.ErrorCode errorCode;

    public SGExceptionMatcher(SGException.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public boolean matches(Object item) {
        if (item instanceof SGException) {
            SGException sgException = (SGException) item;
            return sgException.getCode() == errorCode;
        }
        return false;
    }

    public void describeTo(Description description) {

    }
}
