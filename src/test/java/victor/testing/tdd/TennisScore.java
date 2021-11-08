package victor.testing.tdd;


public class TennisScore {
   private int scorePlayer1;
   private int scorePlayer2;

   public String getScore() {
      if (scorePlayer1 == scorePlayer2 && scorePlayer1 >= 3) {
         return "Deuce";
      }
      String score1 = translatePoints(scorePlayer1);
      String score2 = translatePoints(scorePlayer2);
      return score1 + "-" + score2;
   }

   private String translatePoints(int points) {
      switch (points) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         case 3:
            return "Forty";
      }
      throw new IllegalArgumentException("Nu stiu");
   }

   public void winsPoint(Player player) {
      if (player == Player.ONE) {
         scorePlayer1++;
      } else {
         scorePlayer2++;
      }
   }

   // METODA PUSA IN PROD DOAR PENTRU TESTARE
   public void setPoints(Player player, int points) {
      for (int i = 0; i < points; i++) {
         winsPoint(player);
      }
   }
}
