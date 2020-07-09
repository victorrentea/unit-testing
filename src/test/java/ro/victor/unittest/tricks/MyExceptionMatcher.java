package ro.victor.unittest.tricks;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import ro.victor.unittest.MyException;

public class MyExceptionMatcher extends BaseMatcher<MyException> {

   private final MyException.ErrorCode expectedErrorCode;

   public MyExceptionMatcher(MyException.ErrorCode expectedErrorCode) {
      this.expectedErrorCode = expectedErrorCode;
   }

   @Override
   public boolean matches(Object item) {
      if (!(item instanceof MyException)) {
         return false;
      }
      MyException e = (MyException) item;
      return e.getCode() == expectedErrorCode;
   }

   @Override
   public void describeTo(Description description) {

   }

}
