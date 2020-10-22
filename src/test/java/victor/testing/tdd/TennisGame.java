package victor.testing.tdd;

public class TennisGame {
	private String score= "Love-Love";

	// TODO enum de scoruri
	// TODO o clasa Player care sa aiba 2 chestii:: un enum de aia si un score
	// fiecare score separat (enum, string, clasa separata cu un tostring, ) pentru fiecar player
	// Map<cheie:playerType enum, score: int>  -- marian: "momentan nu-mi trebuie clasa separata. Dupa, daca e nevoie"
	// si mai simplu: int[] - mereu cu size=2
	// si mai simplu: doua int-uri 

	// Scopul TDD este sa-ti permita sa incerci TOATE cele de mai sus. Fara frica ca strici ceva. 
	// Si sa decizi dupa care-i mai simpla dintre solutiile astea 
	
	enum Player {
		ONE
	}

	public String score() {
		return score;
	}

	public void scorePoint(Player player) {
		if ("Fifteen-Love".equals(score)) {
			score = "Thirty-Love";
		} else {
			score = "Fifteen-Love";
		}
	}

}
