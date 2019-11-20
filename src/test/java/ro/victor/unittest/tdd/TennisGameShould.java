package ro.victor.unittest.tdd;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TennisGameShould {

    private String scoreForPoints(int player1Points, int player2Points) {
        TennisGame tennisGame = new TennisGame();
        setPointsForPlayer(tennisGame, 1, player1Points);
        setPointsForPlayer(tennisGame, 2, player2Points);
        return tennisGame.score();
    }

    private void setPointsForPlayer(TennisGame tennisGame, int playerNo, int points) {
        for (int i = 0; i < points; i++) {
            tennisGame.point(playerNo);
        }
    }

    @Test
    public void returnLoveLoveForNewGame() {
        assertThat(scoreForPoints(0,0)).isEqualTo("Love Love");
    }
    @Test
    public void returnFifteenLoveWhenPlayer1Scores1() {
        assertThat(scoreForPoints(1,0)).isEqualTo("Fifteen Love");
    }
    @Test
    public void returnLoveFifteenWhenPlayer2Scores1() {
        assertThat(scoreForPoints(0,1)).isEqualTo("Love Fifteen");
    }
    @Test
    public void returnFifteenFifteenWhenBothPlayersScore1() {
        assertThat(scoreForPoints(1,1)).isEqualTo("Fifteen Fifteen");
    }
    @Test
    public void returnThirtyLoveWhenPlayers1Scores2() {
        assertThat(scoreForPoints(2,0)).isEqualTo("Thirty Love");
    }
    @Test
    public void returnFortyLoveWhenPlayers1Scores3() {
        assertThat(scoreForPoints(3,0)).isEqualTo("Forty Love");
    }
    @Test
    public void returnDeuceWhenBothPlayersScore3() {
        assertThat(scoreForPoints(3, 3)).isEqualTo("Deuce");
    }
    @Test
    public void returnDeuceWhenBothPlayersScore4() {
        assertThat(scoreForPoints(4, 4)).isEqualTo("Deuce");
    }
    @Test
    public void returnGameWon1WhenPlayer1Scores() {
        assertThat(scoreForPoints(4, 0)).isEqualTo("Game Won Player1");
    }
    @Test
    public void returnGameWon2WhenPlayer2Scores() {
        assertThat(scoreForPoints(4, 6)).isEqualTo("Game Won Player2");
    }
    @Test
    public void returnGameWon1WhenPlayer1Scores6_4() {
        assertThat(scoreForPoints(0, 4)).isEqualTo("Game Won Player2");
    }
//    @Test
//    public void returnGameWon1WhenPlayer1Scores() {
//        assertThat(scoreForPoints(4, 0)).isEqualTo("Game Won Player1");
//    }

    @Test
    public void returnAdvantagePlayer1WhenPlayer1Scores4AndPlayer2Scores3() {
        assertThat(scoreForPoints(4, 3)).isEqualTo("Advantage Player1");
    }
    @Test
    public void returnAdvantagePlayer2WhenPlayer2Scores4AndPlayer1Scores3() {
        assertThat(scoreForPoints(3, 4)).isEqualTo("Advantage Player2");
    }

}
