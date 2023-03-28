package victor.testing.design.time;

public class OtherProdCode {
  private final TimeBasedLogic timeBasedLogic;

  public OtherProdCode(TimeBasedLogic timeBasedLogic) {
    this.timeBasedLogic = timeBasedLogic;
  }

  public void method() {
    timeBasedLogic.isFrequentBuyerInternal(13, null);
  }
}
