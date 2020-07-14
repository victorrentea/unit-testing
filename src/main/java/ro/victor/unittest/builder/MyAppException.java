package ro.victor.unittest.builder;

public class MyAppException extends RuntimeException {
	private final ErrorCode errorCode;

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public MyAppException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public MyAppException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public MyAppException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public MyAppException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public MyAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.errorCode = errorCode;
	}
}
