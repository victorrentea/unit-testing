package victor.testing.goldenmaster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

public class TriviaOriginal implements ITrivia {

    private Consumer<Object> writerFunction = System.out::println;

    @Override
    public void setWriterFunction__dinTeste(Consumer<Object> writerFunction) {
        this.writerFunction = writerFunction;
    }

    private void write(Object text) {
        writerFunction.accept(text);
    }



    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses = new int[6];

    boolean[] inPenaltyBox = new boolean[6];
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();

    LinkedList rockQuestions = new LinkedList();
    int currentPlayer = 0;

    boolean isGettingOutOfPenaltyBox;

    public TriviaOriginal() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public boolean isPlayable() {
        return (howManyPlayers() >= 2);
    }

    public void add(String playerName) {


        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        write(playerName + " was added");
        write("They are player number " + players.size());
    }

    public int howManyPlayers() {
        return players.size();
    }

    public void roll(int roll) {
        write(players.get(currentPlayer) + " is the current player");
        write("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                write(players.get(currentPlayer) + " is getting out of the penalty box");
                places[currentPlayer] = places[currentPlayer] + roll;
                if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

                write(players.get(currentPlayer)
                        + "'s new location is "
                        + places[currentPlayer]);
                write("The category is " + currentCategory());
                askQuestion();
            } else {
                write(players.get(currentPlayer) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {

            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

            write(players.get(currentPlayer)
                    + "'s new location is "
                    + places[currentPlayer]);
            write("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void askQuestion() {
        if (currentCategory() == "Pop")
            write(popQuestions.removeFirst());
        if (currentCategory() == "Science")
            write(scienceQuestions.removeFirst());
        if (currentCategory() == "Sports")
            write(sportsQuestions.removeFirst());
        if (currentCategory() == "Rock")
            write(rockQuestions.removeFirst());
    }


    private String currentCategory() {
        if (places[currentPlayer] == 0) return "Pop";
        if (places[currentPlayer] == 4) return "Pop";
        if (places[currentPlayer] == 8) return "Pop";
        if (places[currentPlayer] == 1) return "Science";
        if (places[currentPlayer] == 5) return "Science";
        if (places[currentPlayer] == 9) return "Science";
        if (places[currentPlayer] == 2) return "Sports";
        if (places[currentPlayer] == 6) return "Sports";
        if (places[currentPlayer] == 10) return "Sports";
        return "Rock";
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]) {
            if (isGettingOutOfPenaltyBox) {
                write("Answer was correct!!!!");
                purses[currentPlayer]++;
                write(players.get(currentPlayer)
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }


        } else {

            write("Answer was corrent!!!!");
            purses[currentPlayer]++;
            write(players.get(currentPlayer)
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    public boolean wrongAnswer() {
        write("Question was incorrectly answered");
        write(players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
