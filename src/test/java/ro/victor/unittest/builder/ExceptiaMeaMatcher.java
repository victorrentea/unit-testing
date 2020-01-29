package ro.victor.unittest.builder;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class ExceptiaMeaMatcher extends BaseMatcher<RuntimeException> {
    private final ExceptiaMea.ErrorCode expectedCode;

    ExceptiaMeaMatcher(ExceptiaMea.ErrorCode expectedCode) {
        this.expectedCode = expectedCode;
    }

    @Override
    public void describeTo(Description description) {

    }

    @Override
    public boolean matches(Object item) {
        if (item instanceof ExceptiaMea) {
            ExceptiaMea exceptiaMea = (ExceptiaMea) item;
            return exceptiaMea.getErrorCode() == expectedCode;
        }
        return false;
    }
}
