package victor.testing.tdd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TennisGameTest {

   TennisGame game = new TennisGame();

   public TennisGameTest() {
      System.out.println("Ma instantiez pt fiecare metoda de @Test > asta iti permite sa lasi pe campuri date fara grija ca urmatorul le gaseste tot pe alea.");
   }
   @Test
   void returns_love_love_for_new_game() {
      String actual = game.getScore();

      assertEquals("Love Love", actual);
   }

   @Test
   void returns_love_fifteen_when_player2_scores_1_point() {
      game.playerScores(2);
      String actual = game.getScore();

      assertEquals("Love Fifteen", actual);
   }
   @Test
   void returns_fifteen_love_when_player1_scores_1_point() {
      game.playerScores(1);
      String actual = game.getScore();

      assertEquals("Fifteen Love", actual);
   }

   @Test
   void returns_love_thirty_when_player2_scores_2_point() {
      game.playerScores(2);
      game.playerScores(2);
      String actual = game.getScore();

      assertEquals("Love Thirty", actual);
   }
   @Test
   void returns_love_forty_when_player2_scores_3_point() {
      game.playerScores(2);
      game.playerScores(2);
      game.playerScores(2);
      String actual = game.getScore();

      assertEquals("Love Forty", actual);
   }
   @Test
   void returns_deuce_when_both_players_score_3_points() {
      addPointsToBoth(game, 3, 3);
      String actual = game.getScore();

      assertEquals("Deuce", actual);
   }
   @Test
   void returns_deuce_when_both_players_score_4_points() {
      addPointsToBoth(game, 4, 4);
      String actual = game.getScore();
      assertEquals("Deuce", actual);
   }

   private void addPointsToBoth(TennisGame game, int points1, int points2) {
      addPointsToPlayer(game, 1, points1);
      addPointsToPlayer(game, 2, points2);
   }

   private void addPointsToPlayer(TennisGame game, int playerNumber, int points) {
      for (int i = 0; i < points; i++) {
         game.playerScores(playerNumber);
      }
   }
}


