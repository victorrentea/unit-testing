package ro.victor.unittest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

public class ExceptionalBizTest {

    //ExceptionalBizShould.throwExceptionWhenPassedTrue
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void success() {
        new ExceptionalBiz().crackyBiz(0);
    }

    @Test
    public void throwExceptionWhenPassed1() {
        exception.expect(new MyExceptionMatcher(MyException.ErrorCode.CODE1));
        new ExceptionalBiz().crackyBiz(1);
    }

    @Test
    public void throwExceptionWhenPassed2() {
        exception.expect(new MyExceptionMatcher(MyException.ErrorCode.CODE2));
        new ExceptionalBiz().crackyBiz(2);

    }

    @Test
    public void throwExceptionWhenPassed11() {
        exception.expect(new MyExceptionMatcher(MyException.ErrorCode.CODE11));
        new ExceptionalBiz().crackyBiz(11);
    }
}

class MyExceptionMatcher extends BaseMatcher<MyException> {
    private final MyException.ErrorCode expectedCode;

    MyExceptionMatcher(MyException.ErrorCode expectedCode) {
        this.expectedCode = expectedCode;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof MyException)) return false;
        MyException e = (MyException) item;
        return e.getCode() == expectedCode;
    }
    @Override
    public void describeTo(Description description) {

    }

    public static void main(String[] args) {
        UUID.fromString("a");
    }
}
