package victor.testing.tennis;

import org.springframework.stereotype.Component;

@Component
public class TennisCucumberContext {

  private TennisScore tennisScore;

  public TennisScore getTennisScore() {
    return tennisScore;
  }

  public void setTennisScore(TennisScore tennisScore) {
    this.tennisScore = tennisScore;
  }
}
