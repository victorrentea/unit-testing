package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiagnosticTest {

  @Test
  void checkTransmission() {
    Diagnostic diagnostic = new Diagnostic();
    // nu vrei sa incluzi in testul tau o clasa
    // complexa sau care cere o DB/API extern
    Client clientMock = Mockito.mock(Client.class); // o instanta surogat din clasa Client
    // - pe care o pot invata ce sa returneze: when..then
    // - intreba ce metode i s-au chemat: verify..
    diagnostic.setTelemetryClient(clientMock);
//    client.setOnlineStatus(true);// corect daca era @Entity sau DTO = ob cu date
    when(clientMock.getOnlineStatus()).thenReturn(true);
    when(clientMock.getVersion()).thenReturn("ver");

    diagnostic.checkTransmission(true);

    //assert+verify
    verify(clientMock).disconnect(true);
  }
}