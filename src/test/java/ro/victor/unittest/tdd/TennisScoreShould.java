package ro.victor.unittest.tdd;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisScoreShould {

   private TennisScore tennisScore = new TennisScore();

   public TennisScoreShould() {
      System.out.println("Noua instanta");
   }

   @Test
   public void returnsLoveLoveForNewGame() {
      String score = tennisScore.score();
      assertThat(score).isEqualTo("Love-Love");
   }
   @Test
   public void returnsFifteenLoveWhenPlayer1Score1Point() {
      tennisScore.pointWon(Player.ONE);
      String score = tennisScore.score();
      assertThat(score).isEqualTo("Fifteen-Love");
   }
   @Test
   public void returnsThirtyLoveWhenPlayer1Score2Points() {
      tennisScore.pointWon(Player.ONE);
      tennisScore.pointWon(Player.ONE);
      String score = tennisScore.score();
      assertThat(score).isEqualTo("Thirty-Love");
   }


      // The running score of each game is described in a manner peculiar
      // to tennis: scores from zero to three points are described as
      // "Love", "Fifteen", "Thirty", and "Forty" respectively.
}
