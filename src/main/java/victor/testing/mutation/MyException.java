package victor.testing.mutation;

import java.util.function.Predicate;

public class MyException extends RuntimeException {
    MyException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    // + other 12 overloads, taking a message string, a cause exception, and combinations

    public enum ErrorCode {
        CUSTOMER_MISSING_CITY,
        CUSTOMER_TOO_SHORT_CITY,
        GENERAL
    }

    private final ErrorCode errorCode;

    // to use in tests:
    public static <T extends Throwable> Predicate<T> hasCode(ErrorCode errorCode) {
        return e -> (e instanceof MyException && ((MyException) e).errorCode == errorCode);
    }
}
