package ro.victor.unittest.goldenmaster;

import java.io.Writer;

public interface ITrivia {
   void setWriter(Writer writer);

   boolean add(String playerName);

   void roll(int roll);

   boolean wasCorrectlyAnswered();

   boolean wrongAnswer();
}
