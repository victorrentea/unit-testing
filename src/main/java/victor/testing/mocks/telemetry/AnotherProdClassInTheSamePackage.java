package victor.testing.mocks.telemetry;

public class AnotherProdClassInTheSamePackage {
  public void method() {
    Diagnostic.configureClient("aaa");// this could not happen before
    //BAD
  }
}
