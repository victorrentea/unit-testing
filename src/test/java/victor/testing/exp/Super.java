package victor.testing.exp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public abstract class Super {
  @BeforeAll
  final static void setup() {
    System.out.println("setup beforeAll in parent");
  }
}

class T1 extends Super {
  @BeforeAll
  static void setup2() {
    System.out.println("setup beforeAll in child");
  }
  @Test
  void experiment() {

  }
}

class T2 extends Super {
  @Test
  void experiment() {

  }
}
