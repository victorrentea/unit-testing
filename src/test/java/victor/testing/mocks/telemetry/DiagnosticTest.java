package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode.NORMAL;


@ExtendWith(MockitoExtension.class)
public class DiagnosticTest {
   @Mock
   private Client clientMock;
   @InjectMocks
   private Diagnostic target;
   @Captor
   private ArgumentCaptor<ClientConfiguration> configCaptor;

   @Test
   void explore() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.receive()).thenReturn("tataie");

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
      verify(clientMock).send(Client.DIAGNOSTIC_MESSAGE); // 99%
      verify(clientMock).receive(); // NOT NEEDED0
      assertThat(target.getDiagnosticInfo()).isEqualTo("tataie");
      verify(clientMock).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(NORMAL);
      assertThat(config.getSessionStart()).isCloseTo(now(), byLessThan(1, SECONDS));
      // not common unless targetting a critical API
//      verify(clientMock,never()).reportBadPayerToGovCreditOffice("qa");
//      verifyNoInteractions(clientMock); //
   }
}
