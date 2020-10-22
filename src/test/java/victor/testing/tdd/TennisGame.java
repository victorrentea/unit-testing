package victor.testing.tdd;

public class TennisGame {
	private int player1Score;
	private int player2Score;

	// TODO enum de scoruri
	// TODO o clasa Player care sa aiba 2 chestii:: un enum de aia si un score
	// fiecare score separat (enum, string, clasa separata cu un tostring, ) pentru
	// fiecar player
	// Map<cheie:playerType enum, score: int> -- marian: "momentan nu-mi trebuie
	// clasa separata. Dupa, daca e nevoie"
	// si mai simplu: int[] - mereu cu size=2
	// si mai simplu: doua int-uri

	// Scopul TDD este sa-ti permita sa incerci TOATE cele de mai sus. Fara frica ca
	// strici ceva.
	// Si sa decizi dupa care-i mai simpla dintre solutiile astea

	enum Player {
		ONE,
		TWO
	}

	public String score() {
		if (player1Score >= 4 && player1Score - player2Score >= 2) {
			return "Game Won Player1";
		}
		if (player2Score >= 4 && player2Score - player1Score >= 2) {
			return "Game Won Player2";
		}
		// for (regula in reguli) daca regula.aplicabila return regula.calcul; Chain of
		// responsibility design pattern
		if (player1Score < 3 || player2Score < 3) {
			return translateScore(player1Score) + "-" + translateScore(player2Score);
		}
		return deuceOrAdvantage();
	}

	private String deuceOrAdvantage() {
		if (player1Score == player2Score) {
			return "Deuce";
		}
		if (player1Score - player2Score == 1) {
			return "Advantage Player1";
		}
		if (player2Score - player1Score == 1) {
			return "Advantage Player2";
		}
		throw new IllegalStateException();
	}

	private String translateScore(int score) {
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
			throw new IllegalStateException();
		}
	}

	public void scorePoint(Player player) {
		if (player == Player.ONE) {
			player1Score++;
		} else {
			player2Score++;
		}

	}

}
