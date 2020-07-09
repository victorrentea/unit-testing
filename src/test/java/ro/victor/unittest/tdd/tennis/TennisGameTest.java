package ro.victor.unittest.tdd.tennis;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {
   @Test
   public void test() {
      assertEquals("Love - Love", TennisGame.score());
   }
}
