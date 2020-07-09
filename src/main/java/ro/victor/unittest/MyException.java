package ro.victor.unittest;

public class MyException extends RuntimeException {
   public enum ErrorCode {
      NEGATIVE_PARAM,
      MISSING_CUSTOMER_NAME, MISSING_CUSTOMER_ADDRESS, MISSING_CUSTOMER_ADDRESS_CITY, PARAM_OVER_10
   }

   private final ErrorCode code;

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
