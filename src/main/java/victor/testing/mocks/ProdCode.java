package victor.testing.mocks;

class ProdCode {

   public int prod(int p) {
      System.out.println("Here");
      return SomeLibrary.staticMethod(p) + 1;
   }
}

class SomeLibrary {
   public static int staticMethod(int p) {
      return -1;
   }
}
