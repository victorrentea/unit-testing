package victor.testing.junit;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class JUnit4Exceptions {

  @Test
  public void terrible() { // never
    try {
      testedWhichShouldThrow();
      fail("Expected RuntimeException");
    } catch (RuntimeException e) {
      if (!e.getMessage().equals("BOOM")) {
        throw new RuntimeException("Expected BOOM but got " + e.getMessage());
      }
    }
  }

  @Test(expected = RuntimeException.class)
  public void testInTheAnnotation() {
    testedWhichShouldThrow();
  }

  @Rule
  public ExpectedException rule = ExpectedException.none();

  @Test
  public void testWithRule() {
    rule.expect(RuntimeException.class);
    rule.expectMessage("BOOM");

    testedWhichShouldThrow();
  }

  @Test
  public void testLambdas() {
//    Assert.assertEquals(); // MIGRATE THIS
//    jupiter...Assertions.assertEquals();// jupiter < DON"T USE THID
//    Assertions.assertThat // assertJ
    var e = assertThrows(RuntimeException.class,
        () -> testedWhichShouldThrow());
    assertEquals("BOOM", e.getMessage());
  }

  @Test
  public void testAssertJ() {
    assertThatThrownBy(() -> testedWhichShouldThrow())
        .isInstanceOf(RuntimeException.class)
        .hasMessage("BOOM");
  }


  private static void testedWhichShouldThrow() {
    throw new RuntimeException("BOOM");
  }
}
