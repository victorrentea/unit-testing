package ro.victor.unittest.tricks;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.victor.unittest.MyException;

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


