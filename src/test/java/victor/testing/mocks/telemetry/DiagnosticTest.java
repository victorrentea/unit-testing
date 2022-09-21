package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
      verify(clientMock).send("AT#UD"); // freezes the constant value
      verify(clientMock).receive(); // NOT NEEDED0
      assertThat(target.getDiagnosticInfo()).isEqualTo("tataie");
      verify(clientMock).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(NORMAL);
      // not common unless targetting a critical API
//      verify(clientMock,never()).reportBadPayerToGovCreditOffice("qa");
//      verifyNoInteractions(clientMock); //
   }
}
