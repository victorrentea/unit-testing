package victor.testing.tennis;

import org.springframework.stereotype.Component;

@Component
public class CucumberTennisContext {
  private TennisScore tennisScore;

  public TennisScore getTennisScore() {
    return tennisScore;
  }

  public void setTennisScore(TennisScore tennisScore) {
    this.tennisScore = tennisScore;
  }
}
