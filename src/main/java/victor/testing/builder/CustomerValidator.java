package victor.testing.builder;

import com.mysql.cj.x.protobuf.Mysqlx.ErrorOrBuilder;
import victor.testing.builder.MyException.ErrorCode;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class CustomerValidator {

	public void validate(Customer customer) {
		if (isBlank(customer.getName())) {
			throw new IllegalArgumentException("Missing customer name");
		}
		validateAddress(customer.getAddress());
		//etc
	}
	
	private void validateAddress(Address address) {

		if (address == null) {
			throw new IllegalArgumentException("Missing customer address");
		}
		if (isBlank(address.getCity())) {
			throw new MyException(ErrorCode.NO_ADDRESS_CITY);
		}
	}
}


class MyException extends RuntimeException {

	public enum ErrorCode {
		NO_ADDRESS,
		NO_ADDRESS_CITY,
		NO_NAME,
		GENERAL
	}
	private final ErrorCode code;
	// String [] params;


	public ErrorCode getCode() {
		return code;
	}

	public MyException(ErrorCode code) {
		this.code = code;
	}

	public MyException(String message, ErrorCode code) {
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
}