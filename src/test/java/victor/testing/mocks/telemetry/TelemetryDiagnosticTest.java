package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode.NORMAL;


@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
   @Mock
   TelemetryClient clientMock;
   @InjectMocks
   TelemetryDiagnostic target;

   @Test
   void throwsWhenNotOnline() {
      assertThatThrownBy( ()-> target.checkTransmission(true) )
              .isInstanceOf(IllegalStateException.class);
   }
   @Test
   void happy() {
      when(clientMock.getOnlineStatus()).thenReturn(true);
      when(clientMock.receive()).thenReturn("aceeasivaloare");

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
      assertThat(target.getDiagnosticInfo()).isEqualTo("aceeasivaloare");
   }

   @Test
   void configHasAckModeNormal() {
      // given
      when(clientMock.getOnlineStatus()).thenReturn(true);

      // when
      target.checkTransmission(true);

      // then
      ArgumentCaptor<ClientConfiguration> configCaptor = ArgumentCaptor.forClass(ClientConfiguration.class);
      verify(clientMock).configure(configCaptor.capture()); // mockule, ti-a chemat produ functia configure? da? cu ce param te rog;
      ClientConfiguration configuration = configCaptor.getValue(); // captorule, da-mi si mie ce ti0-a dat mockul adineauri
      assertThat(configuration.getAckMode()).isEqualTo(NORMAL);
   }



}
