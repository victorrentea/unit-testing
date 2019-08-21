package ro.victor.unittest;

class ExceptionalBiz {
    public void crackyBiz(int b) {
        if (b == 1) {
            throw new MyException(MyException.ErrorCode.CODE1);
        }
        if (b == 2) {
            throw new MyException(MyException.ErrorCode.CODE2);
        }
        if (b == 11) {
            throw new MyException(MyException.ErrorCode.CODE1);
        }
    }
}

class MyException extends RuntimeException {
    public enum ErrorCode {
        GENERAL, CODE1, CODE2, CODE11
    }
    private final ErrorCode code;

    public MyException(ErrorCode code) {
        this.code = code;
    }
    public MyException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public MyException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;
    }

    public MyException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;
    }

    public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}