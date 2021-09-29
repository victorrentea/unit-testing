package victor.testing.mocks;

class A {
   public int method() {
      System.out.println("Here");
      return SomeUtil.staticMethod(3) + 1;
   }
}
