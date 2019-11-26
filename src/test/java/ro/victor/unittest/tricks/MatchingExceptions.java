package ro.victor.unittest.tricks;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MatchingExceptions {
    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test(expected = ExceptiaMea.class)
    public void throwsForNegativeParam_ingineresteVorbind() {
        ClasaDeTestat.cevaGreuCareMaiSiCrapa(-1);
    }
    @Test
    public void throwsForNegativeParam() {
        exception.expect(new ExceptiaMeaMatcher(ExceptiaMea.ErrorCode.NEGATIVE_PARAM));
        ClasaDeTestat.cevaGreuCareMaiSiCrapa(-1);
    }

    @Test
    public void throwsForParamOver10() {
        exception.expect(new ExceptiaMeaMatcher(ExceptiaMea.ErrorCode.PARAM_OVER_10));
        ClasaDeTestat.cevaGreuCareMaiSiCrapa(11);
    }

    @Test
    public void okForParam9() {
        ClasaDeTestat.cevaGreuCareMaiSiCrapa(9);
    }
}

class ExceptiaMeaMatcher extends BaseMatcher<ExceptiaMea> {

        private final ExceptiaMea.ErrorCode negativeParam;

        ExceptiaMeaMatcher(ExceptiaMea.ErrorCode negativeParam) {
            this.negativeParam = negativeParam;
        }

        @Override
        public boolean matches(Object item) {
            if (!(item instanceof ExceptiaMea)) {
                return false;
            }
            ExceptiaMea e = (ExceptiaMea) item;
            return e.getCode() == negativeParam;
        }

        @Override
        public void describeTo(Description description) {

        }

}

class ClasaDeTestat {
    public static void cevaGreuCareMaiSiCrapa(int i) {

        if (i < 0) {
            throw new ExceptiaMea(ExceptiaMea.ErrorCode.NEGATIVE_PARAM);
        }


        if (i > 10) {
            throw new ExceptiaMea(ExceptiaMea.ErrorCode.PARAM_OVER_10);
        }
    }
}


class ExceptiaMea extends RuntimeException {
    public enum ErrorCode {
        NEGATIVE_PARAM,
        PARAM_OVER_10
    }

    private final ErrorCode code;

    public ExceptiaMea(ErrorCode code) {
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}