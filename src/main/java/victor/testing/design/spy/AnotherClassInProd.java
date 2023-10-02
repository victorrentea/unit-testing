package victor.testing.design.spy;

public class AnotherClassInProd {
  BigService bigService;

  public void illegalAccessToVisibleForTesting() {
//    bigService.low(new Order());
  }
}
