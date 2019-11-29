package ro.victor.unittest.tricks;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MatchingExceptions {
    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test(expected = MyException.class)
    public void throwsForNegativeParam_ingineresteVorbind() {
        ClassToTest.cevaGreuCareMaiSiCrapa(-1);
    }
    @Test
    public void throwsForNegativeParam() {
        exception.expect(new MyExceptionMatcher(MyException.ErrorCode.NEGATIVE_PARAM));
        ClassToTest.cevaGreuCareMaiSiCrapa(-1);
    }

    @Test
    public void throwsForParamOver10() {
        exception.expect(new MyExceptionMatcher(MyException.ErrorCode.PARAM_OVER_10));
        ClassToTest.cevaGreuCareMaiSiCrapa(11);
    }

    @Test
    public void okForParam9() {
        ClassToTest.cevaGreuCareMaiSiCrapa(9);
    }
}

class MyExceptionMatcher extends BaseMatcher<MyException> {

        private final MyException.ErrorCode negativeParam;

        MyExceptionMatcher(MyException.ErrorCode negativeParam) {
            this.negativeParam = negativeParam;
        }

        @Override
        public boolean matches(Object item) {
            if (!(item instanceof MyException)) {
                return false;
            }
            MyException e = (MyException) item;
            return e.getCode() == negativeParam;
        }

        @Override
        public void describeTo(Description description) {

        }

}

class ClassToTest {
    public static void cevaGreuCareMaiSiCrapa(int i) {

        if (i < 0) {
            throw new MyException(MyException.ErrorCode.NEGATIVE_PARAM);
        }


        if (i > 10) {
            throw new MyException(MyException.ErrorCode.PARAM_OVER_10);
        }
    }
}


class MyException extends RuntimeException {
    public enum ErrorCode {
        NEGATIVE_PARAM,
        PARAM_OVER_10
    }

    private final ErrorCode code;

    public MyException(ErrorCode code) {
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}