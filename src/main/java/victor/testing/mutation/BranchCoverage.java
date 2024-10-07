package victor.testing.mutation;

public class BranchCoverage {
  public static void f(int i, int j) {
    if (j == 1 && i == 1) {
      System.out.println("In");
    }
    System.out.println("After");
  }
}
