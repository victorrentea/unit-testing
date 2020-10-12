package victor.testing.tdd;


import org.junit.jupiter.api.Test;
import victor.testing.tdd.TennisGame.Player;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {
   public TennisGameTest() {
//      System.out.println("O instanta noua per @Test!");
   }

   private String translateScore(int points1, int points2) {
      TennisGame tennisGame = new TennisGame();
      markPoints(Player.ONE, points1, tennisGame);
      markPoints(Player.TWO, points2, tennisGame);
      return tennisGame.score();
   }

   private void markPoints(Player player, int points, TennisGame tennisGame) {
      for (int i = 0; i < points; i++) {
         tennisGame.markPoint(player);
      }
   }
   @Test
   public void loveLove() {
      assertThat(translateScore(0,0)).isEqualTo("Love - Love");
   }


   @Test
   public void loveFifteen() {
      assertThat(translateScore(0,1)).isEqualTo("Love - Fifteen");
   }
   @Test
   public void fifteenLove() {
      assertThat(translateScore(1,0)).isEqualTo("Fifteen - Love");
   }
   @Test
   public void loveThirty() {
      assertThat(translateScore(0,2)).isEqualTo("Love - Thirty");
   }
   @Test // il las chiar daca e un pic overlapping, pentru ca e un caz de biz interesant:
   // Faptul ca ambii pot castiga puncte
   public void fifteenFifteen() {
      assertThat(translateScore(1,1)).isEqualTo("Fifteen - Fifteen");
   }
   @Test
   public void loveForty() {
      assertThat(translateScore(0, 3)).isEqualTo("Love - Forty");
   }
   @Test
   public void deuce() {
      assertThat(translateScore(3, 3)).isEqualTo("Deuce");
   }

   @Test
   public void deuce4() {
      assertThat(translateScore(4, 4)).isEqualTo("Deuce");
   }


}
