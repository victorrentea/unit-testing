package victor.testing.mocks.telemetry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AltaClasaDinProd {
  private final Diagnostic diagnostic
      ;

  public void method() {
    // NU AR TREBUI SA POATA VEDEA FUNCTIA DESHISA DOAR PENTRU TESTARE
    diagnostic.createConfig(false);
  }
}
