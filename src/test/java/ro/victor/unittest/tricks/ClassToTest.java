package ro.victor.unittest.tricks;

import ro.victor.unittest.MyException;

public class ClassToTest {
   public static void cevaGreuCareMaiSiCrapa(int i) {

      if (i < 0) {
         throw new MyException(MyException.ErrorCode.NEGATIVE_PARAM);
      }


      if (i > 10) {
         throw new MyException(MyException.ErrorCode.PARAM_OVER_10);
      }
   }
}
