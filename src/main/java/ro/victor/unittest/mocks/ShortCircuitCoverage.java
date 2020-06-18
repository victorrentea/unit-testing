package ro.victor.unittest.mocks;

public class ShortCircuitCoverage {
   public void m(boolean b) {
      if (b && f() || g()) {
         System.out.println("HALO");
      }
   }

   private boolean f() {
      return true;
   }

   private boolean g() {
      return false;
   }
}
