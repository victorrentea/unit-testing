package victor.testing.mocks;

class LegacyProdCode {

   public int prod() {
      System.out.println("Logic to test");
      return SomeUtil.staticMethod(3) + 1;
   }
}


class SomeUtil {
   public static int staticMethod(int x) {
      return -1;
   }
}
