package victor.testing.tdd;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import victor.testing.tdd.TennisGame.Player;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
   private final TennisGame tennisGame = new TennisGame();
   public TennisGameTest() {
//      System.out.println("O instanta noua per @Test!");
   }
   @Test
   public void loveLove() {
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Love - Love");
   }
   @Test
   public void loveFifteen() {
      tennisGame.markPoint(Player.TWO);
      String score = tennisGame.score();

      assertThat(score).isEqualTo("Love - Fifteen");
   }
   @Test
   public void fifteenLove() {
      tennisGame.markPoint(Player.ONE);
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Fifteen - Love");
   }
   @Test
   public void loveThirty() {
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Love - Thirty");
   }
   @Test // il las chiar daca e un pic overlapping, pentru ca e un caz de biz interesant:
   // Faptul ca ambii pot castiga puncte
   public void fifteenFifteen() {
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.TWO);
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Fifteen - Fifteen");
   }
   @Test
   public void loveForty() {
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Love - Forty");
   }
   @Test
   public void deuce() {
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Deuce");
   }
   @Test
   public void deuce4() {
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.ONE);
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      tennisGame.markPoint(Player.TWO);
      String score = tennisGame.score();
      assertThat(score).isEqualTo("Deuce");
   }
}
