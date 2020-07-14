package ro.victor.unittest.builder;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

class MyAppExceptionMatcher extends BaseMatcher<Exception> {
   private final ErrorCode expectedErrorCode;

   public MyAppExceptionMatcher(ErrorCode expectedErrorCode) {
      this.expectedErrorCode = expectedErrorCode;
   }

   @Override
   public boolean matches(Object item) {
      if (!(item instanceof MyAppException)) {
         return false;
      }
      MyAppException myAppException = (MyAppException) item;
      return myAppException.getErrorCode() == expectedErrorCode;
   }

   @Override
   public void describeTo(Description description) {
 // TODO
   }
}
