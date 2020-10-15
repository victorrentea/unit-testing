package victor.testing.tdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.Arguments.of;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import victor.testing.tdd.TennisGame.Player;

import java.util.stream.Stream;

public class TennisGameParameterizedTest {

	public static Stream<Arguments> data() {
		return  Stream.of(
				of(0, 0, "Love:Love"),
				of(1, 2, "Fifteen:Thirty")
				);
	}

	@ParameterizedTest
	@MethodSource("data")
	public void theTest(int player1Points, int player2Points, String expectedScore) throws Exception {
		String score = getScore(player1Points, player2Points);
		assertThat(score).isEqualTo(expectedScore);

	}

	private String getScore(int player1Points, int player2Points) {
		TennisGame tennisGame = new TennisGame();
		setPointsForPlayer(player1Points, Player.ONE, tennisGame);
		setPointsForPlayer(player2Points, Player.TWO, tennisGame);
		return tennisGame.score();
	}

	private void setPointsForPlayer(int points, Player player, TennisGame tennisGame) {
		for (int i = 0; i < points; i++) {
			tennisGame.playerScoresPoint(player);
		}
	}

}
