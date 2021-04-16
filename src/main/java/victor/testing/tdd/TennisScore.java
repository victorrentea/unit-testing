package victor.testing.tdd;

public class TennisScore {

   private int homeScore;
   private int awayScore;

   public String getScore() {
      if (homeScore == awayScore && homeScore >= 3) {
         return "Deuce";
      }
      return translate(homeScore) + " - " + translate(awayScore);
   }
   public String translate(int score) {
      switch (score) {
         case 0:
            return "Love";
         case 1:
            return "Fifteen";
         case 2:
            return "Thirty";
         case 3:
            return "Forty";
         default:
            throw new IllegalStateException("Unexpected value: " + score);
      }
   }

   public void addPoint(Player player) {
      if (player == Player.AWAY) {
         awayScore++;
      } else {
         homeScore++;
      }
   }
}
