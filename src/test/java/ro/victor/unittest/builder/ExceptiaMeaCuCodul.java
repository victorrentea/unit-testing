package ro.victor.unittest.builder;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class ExceptiaMeaCuCodul extends BaseMatcher<ExceptiaMea> {
    private final ExceptiaMea.ErrorCode expectedCode;

    public ExceptiaMeaCuCodul(ExceptiaMea.ErrorCode expectedCode) {
        this.expectedCode = expectedCode;
    }

    @Override
    public boolean matches(Object item) {
        ExceptiaMea e = (ExceptiaMea) item;
        return e.getErrorCode() == expectedCode;
    }

    @Override
    public void describeTo(Description description) {

    }
}
