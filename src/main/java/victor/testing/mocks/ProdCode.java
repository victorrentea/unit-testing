package victor.testing.mocks;

class ProdCode {

   public int prod(int p) {
      System.out.println("Here");
      return SomeLibrary.staticMethod(p) + 1;
   }
}
