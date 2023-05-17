package victor.testing.mocks.telemetry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DiagnosticTest {
  /**
   * Method under test: {@link Diagnostic#checkTransmission(boolean)}
   */
  @Test
  @Disabled("TODO: Complete this test")
  void testCheckTransmission() {
    // TODO: Complete this test.
    //   Reason: R013 No inputs found that don't throw a trivial exception.
    //   Diffblue Cover tried to run the arrange/act section, but the method under
    //   test threw
    //   java.lang.IllegalStateException: Performs some external remote call impossible/not desirable to call from automated tests.
    //       at victor.testing.mocks.telemetry.Client.disconnect(Client.java:64)
    //       at victor.testing.mocks.telemetry.Diagnostic.checkTransmission(Diagnostic.java:21)
    //   See https://diff.blue/R013 to resolve this issue.

    (new Diagnostic(new Client())).checkTransmission(true);
  }

  /**
   * Method under test: {@link Diagnostic#checkTransmission(boolean)}
   */
  @Test
  void testCheckTransmission2() {
    Client client = mock(Client.class);
    when(client.getVersion()).thenReturn("1.0.2");
    when(client.receive()).thenReturn("Receive");
    doNothing().when(client).configure(Mockito.<Client.ClientConfiguration>any());
    doNothing().when(client).send(Mockito.<String>any());
    when(client.getOnlineStatus()).thenReturn(true);
    doNothing().when(client).connect(Mockito.<String>any());
    doNothing().when(client).disconnect(anyBoolean());
    Diagnostic diagnostic = new Diagnostic(client);

    diagnostic.checkTransmission(true);

    verify(client, atLeast(1)).getOnlineStatus();
    verify(client).getVersion();
    verify(client).receive();
    verify(client).configure(Mockito.<Client.ClientConfiguration>any());
    verify(client).disconnect(anyBoolean());
    verify(client).send(Mockito.<String>any());
    assertEquals("Receive", diagnostic.getDiagnosticInfo());
  }

  /**
   * Method under test: {@link Diagnostic#checkTransmission(boolean)}
   */
  @Test
  @Disabled("TODO: Complete this test")
  void testCheckTransmission3() {
    // TODO: Complete this test.
    //   Reason: R013 No inputs found that don't throw a trivial exception.
    //   Diffblue Cover tried to run the arrange/act section, but the method under
    //   test threw
    //   java.lang.IllegalStateException: AT#UD
    //       at victor.testing.mocks.telemetry.Client.getVersion(Client.java:59)
    //       at victor.testing.mocks.telemetry.Diagnostic.checkTransmission(Diagnostic.java:32)
    //   See https://diff.blue/R013 to resolve this issue.

    Client client = mock(Client.class);
    when(client.getVersion()).thenThrow(new IllegalStateException(Client.DIAGNOSTIC_MESSAGE));
    when(client.receive()).thenThrow(new IllegalStateException(Client.DIAGNOSTIC_MESSAGE));
    doThrow(new IllegalStateException(Client.DIAGNOSTIC_MESSAGE)).when(client)
        .configure(Mockito.<Client.ClientConfiguration>any());
    doThrow(new IllegalStateException(Client.DIAGNOSTIC_MESSAGE)).when(client).send(Mockito.<String>any());
    when(client.getOnlineStatus()).thenReturn(true);
    doNothing().when(client).connect(Mockito.<String>any());
    doNothing().when(client).disconnect(anyBoolean());
    (new Diagnostic(client)).checkTransmission(true);
  }

  /**
   * Method under test: {@link Diagnostic#checkTransmission(boolean)}
   */
  @Test
  void testCheckTransmission4() {
    Client client = mock(Client.class);
    when(client.getVersion()).thenReturn("1.0.2");
    when(client.receive()).thenReturn("Receive");
    doNothing().when(client).configure(Mockito.<Client.ClientConfiguration>any());
    doNothing().when(client).send(Mockito.<String>any());
    when(client.getOnlineStatus()).thenReturn(false);
    doNothing().when(client).connect(Mockito.<String>any());
    doNothing().when(client).disconnect(anyBoolean());
    assertThrows(IllegalStateException.class, () -> (new Diagnostic(client)).checkTransmission(true));
    verify(client, atLeast(1)).getOnlineStatus();
    verify(client, atLeast(1)).connect(Mockito.<String>any());
    verify(client).disconnect(anyBoolean());
  }
}

