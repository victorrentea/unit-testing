package victor.testing.tdd;

//   Player player1;
//   Player player1;
public class TennisGame {

   private int score1;
   private int score2;

   public void playerScores(int playerNumber) {
      if (playerNumber == 1) {
         score1 ++;
      } else {
         score2 ++;
      }
   }

   public String getScore() {
      if (score1 >= 3 && score2 >= 3 && score1 == score2) {
         return "Deuce";
      }
      return translate(score1) + " " + translate(score2);
   }

   private String translate(int score) {
//      Map<Integer, String>
      // String[]
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
            throw new IllegalArgumentException(" NU stiu ce sa fac");
      }
   }
}

