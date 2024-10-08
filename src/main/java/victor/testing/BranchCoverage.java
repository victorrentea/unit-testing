package victor.testing;

import java.util.List;

public class BranchCoverage {
  public static void f(int i, int j) {
    if (i == 1 && j == 1) {
      System.out.println("In");
    }
    System.out.println("After");
  }

  public static void g(int i, int j) {
    if (i == 1) {
      System.out.println("i");
    }
    if (j == 1) {
      System.out.println("j");
    }
    System.out.println("After");
  }

  public static void h(List<String> list) {
    for (String s : list) {
      System.out.println(s);
    }
  }
}
