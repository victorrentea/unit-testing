package victor.testing.tdd;

public class TennisScore {

   private static String string = "Love-Love";

   enum Player {
      ONE;
   }


   public static String getScore() {
      return string;
   }
   public static void winsPoint(Player one) {
      string = "Fifteen-Love";
   }
}
