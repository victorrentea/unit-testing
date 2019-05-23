package ro.victor;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExceptieCuCod {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void test1() {
//		expectedException.expectMessage("a");
		expectedException.expect(expectedCode(MyAppException.ErrorCode.GENERAL));
		aruncama();
	}

	private MyExceptionMatcher expectedCode(MyAppException.ErrorCode code) {
		return new MyExceptionMatcher(code);
	}


	public void aruncama() {
		if (true) throw new MyAppException(MyAppException.ErrorCode.GENERAL);
		if (true) throw new IllegalArgumentException("a");
		if (true) throw new IllegalArgumentException("b");
		if (true) throw new IllegalArgumentException("c");
		if (true) throw new IllegalArgumentException("c");
	}
}

class CuDate {
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String f;

	public String getA() {
		return a;
	}

	public CuDate setA(String a) {
		this.a = a;
		return this;
	}

	public String getB() {
		return b;
	}

	public CuDate setB(String b) {
		this.b = b;
		return this;
	}

	public String getC() {
		return c;
	}

	public CuDate setC(String c) {
		this.c = c;
		return this;
	}

	public String getD() {
		return d;
	}

	public CuDate setD(String d) {
		this.d = d;
		return this;
	}

	public String getE() {
		return e;
	}

	public CuDate setE(String e) {
		this.e = e;
		return this;
	}

	public String getF() {
		return f;
	}

	public CuDate setF(String f) {
		this.f = f;
		return this;
	}

	{
		new CuDate()
				.setA("a")
				.setB("b");
	}
}

class MyAppException extends RuntimeException {
	MyAppException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	enum ErrorCode {
		GENERAL,
		DUPLICATE_USENAME
	}
	private final ErrorCode errorCode;

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}


class MyExceptionMatcher extends BaseMatcher<MyAppException> {

	private final MyAppException.ErrorCode expectedCode;

	MyExceptionMatcher(MyAppException.ErrorCode expectedCode) {
		this.expectedCode = expectedCode;
	}


	public boolean matches(Object item) {
		MyAppException ex = (MyAppException) item;
		return ex.getErrorCode() == expectedCode;
		}

		public void describeMismatch(Object item, Description mismatchDescription) {

		}

		public void describeTo(Description description) {

		}
		}