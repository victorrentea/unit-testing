package ro.victor.unittest.tdd;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameTest {

   private final TennisGame tennisGame = new TennisGame();


   private String translate(int player1Score, int player2Score) { // e pura ?
      addPointsToPlayer(Players.PLAYER1, player1Score);
      addPointsToPlayer(Players.PLAYER2, player2Score);
      return tennisGame.score();
   }

   private void addPointsToPlayer(Players player, int points) {
      for (int i = 0; i < points; i++) {
         tennisGame.scoresPoint(player);
      }
   }
   @Test
   public void newGame() {
      assertThat(translate(0, 0)).isEqualTo("Love - Love");
   }

   @Test
   public void loveFifteen() {
      assertThat(translate(0, 1)).isEqualTo("Love - Fifteen");
      assertThat(translate(0, 1)).isEqualTo("Love - Fifteen");
   }

   @Test
   public void fifteenFifteen() {
      assertThat(translate(1, 1)).isEqualTo("Fifteen - Fifteen");
   }

   @Test
   public void fifteenLove() {
      assertThat(translate(1,0 )).isEqualTo("Fifteen - Love");
   }

   @Test
   public void thirtyLove() {
      assertThat(translate(2, 0)).isEqualTo("Thirty - Love");
   }

   @Test
   public void fortyLove() {
      assertThat(translate(3, 0)).isEqualTo("Forty - Love");
   }

   @Test
   public void deuce() {
      assertThat(translate(3, 3)).isEqualTo("Deuce");
   }

   @Test
   public void deuce4() {
      assertThat(translate(4, 4)).isEqualTo("Deuce");
   }
   @Test
   public void advantage1() {
      assertThat(translate(4, 3)).isEqualTo("Advantage Player1");
   }
   @Test
   public void advantageHorror() {
      assertThat(translate(34, 33)).isEqualTo("Advantage Player1");
   }

}
