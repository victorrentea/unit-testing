package ro.victor.unittest.tdd;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreShould {

   private TennisScore tennisScore = new TennisScore();

   private String translateScore(int score1, int score2) {
      setScoreForPlayer(score1, Player.ONE);
      setScoreForPlayer(score2, Player.TWO);
      return tennisScore.score();
   }
   private void setScoreForPlayer(int points, Player player) {
      for (int i = 0; i < points; i++) {
         tennisScore.pointWon(player);
      }
   }

   @Test
   public void returnsLoveLoveForNewGame() {
      assertThat(translateScore(0, 0)).isEqualTo("Love-Love");
   }
   @Test
   public void returnsFifteenLoveWhenPlayer1Score1Point() {
      assertThat(translateScore(1, 0)).isEqualTo("Fifteen-Love");
   }
   @Test
   public void returnsThirtyLoveWhenPlayer1Score2Points() {
      assertThat(translateScore(2, 0)).isEqualTo("Thirty-Love");
   }
   @Test
   public void returnsLoveFifteenWhenPlayer2Score1Point() {
      assertThat(translateScore(0, 1)).isEqualTo("Love-Fifteen");
   }
   @Test
   public void returnsLoveFifteenWhenPlayer1Score1PointAndPlayer2Scores1Point() {
      assertThat(translateScore(1, 1)).isEqualTo("Fifteen-Fifteen");
   }

   @Test
   public void returnsLoveFortyWhenPlayer2Score3Point() {
      assertThat(translateScore(0, 3)).isEqualTo("Love-Forty");
   }

   @Test
   public void deuce3() {
      assertThat(translateScore(3, 3)).isEqualTo("Deuce");
   }
   @Test
   public void deuce4() {
      assertThat(translateScore(4, 4)).isEqualTo("Deuce");
   }

}
