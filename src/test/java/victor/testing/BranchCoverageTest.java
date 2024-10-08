package victor.testing;

import org.junit.jupiter.api.Test;

import java.util.List;

public class BranchCoverageTest {
  @Test
  void f1() {
    BranchCoverage.f(1,1);
  }
  @Test
  void f2() {
    BranchCoverage.f(1,2);
  }
  @Test
  void f3() {
    BranchCoverage.f(2,1);
  }

  @Test
  void experiment() {
    BranchCoverage.h(List.of("a"));
  }
}
