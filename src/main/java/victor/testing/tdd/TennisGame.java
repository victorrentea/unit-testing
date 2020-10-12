package victor.testing.tdd;

public class TennisGame {


   enum Player {
      ONE,
      TWO
   }

   private int player1Points;
   private int player2Points;

   public void markPoint(Player player) {
      if (player == Player.TWO) {
         player2Points++;
      } else {
         player1Points++;
      }
   }

   public String score() {
      if (player1Points >= 3 && player2Points >= 3 && player1Points == player2Points) {
         return "Deuce";
      }
      return translate(player1Points) + " - " + translate(player2Points);
   }

   private  static final String[] LABELS = {"Love", "Fifteen", "Thirty", "Forty"};

   private String translate(int points) {
      return LABELS[points];
//      switch (points) {
//         case 0:
//            return "Love";
//         case 1:
//            return "Fifteen";
//         case 2:
//            return "Thirty";
//         case 3:
//            return "Forty";
//         default:
//            throw new IllegalStateException("Unexpected value: " + points);
//      }
   }
}
