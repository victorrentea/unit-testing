package victor.testing.goldenmaster;

import java.util.function.Consumer;

public interface ITrivia {
   void add(String playerName);

   void roll(int n);

   boolean wrongAnswer();

   boolean wasCorrectlyAnswered();

   void setWriterFunction__dinTeste(Consumer<Object> writerFunction);
}
