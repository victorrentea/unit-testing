package victor.testing.mutation;

import org.junit.jupiter.api.Test;

public class BranchCoverageTest {
  @Test
  void t1() {
    BranchCoverage.f(1,1);
  }
  @Test
  void t2() {
    BranchCoverage.f(1,2);
  }
  @Test
  void t3() {
    BranchCoverage.f(2,1);
  }
}
