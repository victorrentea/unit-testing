package ro.victor.unittest.tdd.tennis;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TennisGameTest {
   @Test
   public void test() {
      assertEquals("Love - Love", TennisGame.score());
   }
   @Test
   public void fifteenLove() {
      TennisGame.addPoint(1);
      assertEquals("Fifteen - Love", TennisGame.score());
   }
   @Test
   public void loveFifteen() {
      TennisGame.addPoint(2);
      assertEquals("Love - Fifteen", TennisGame.score());
   }

//   @Test
//   public void fifteenFifteen() {
//      TennisGame.addPoint(1);
//      TennisGame.addPoint(2);
//      assertEquals("Fifteen - Fifteen", TennisGame.score());
//   }
}
