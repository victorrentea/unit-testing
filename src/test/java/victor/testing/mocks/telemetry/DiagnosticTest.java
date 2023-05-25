package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.InstantSource;

import static java.time.LocalDateTime.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static victor.testing.mocks.telemetry.Diagnostic.ZONE_ID;


//@ExtendWith(MockitoExtension.class)
public class DiagnosticTest {
//   public static final InstantSource CHRISTMAS = InstantSource.fixed(parse("2023-12-25").toInstant(ZOffZONE_ID));
   //   @Mock
   UUIDFactory uuidFactory = mock(UUIDFactory.class);
   Client client = mock(Client.class); /*new Client() {
   @Override
   public boolean getOnlineStatus() {
      return true;
   }
};*/// = mock(Client.class);
//   @InjectMocks
   Diagnostic target = new Diagnostic(
       client, uuidFactory, null);

   @BeforeEach
   final void before() {
      when(client.getOnlineStatus()).thenReturn(true);
   }

   @Test
   public void disconnects() {
      target.checkTransmission(true);
      verify(client).disconnect(true);
   }

   @Test
   public void throwsWhenNotOnline() {
      when(client.getOnlineStatus()).thenReturn(false);
      assertThatThrownBy(() -> target.checkTransmission(true))
              .isInstanceOf(IllegalStateException.class);
   }

   @Test
   public void sendsDiagnosticInfo() {
      target.checkTransmission(true);
      verify(client).send(Client.DIAGNOSTIC_MESSAGE);
   }

   @Test
   public void receivesDiagnosticInfo() {
      final String diagnosticMessage = "DIAG";
      when(client.receive()).thenReturn(diagnosticMessage);
      target.checkTransmission(true);
      verify(client).receive();
      assertThat(target.getDiagnosticInfo()).isEqualTo(diagnosticMessage);
   }

   //   @Captor
   ArgumentCaptor<ClientConfiguration> configCaptor
       = ArgumentCaptor.forClass(ClientConfiguration.class);

   @Test
   public void configuresClient() throws Exception {
      when(client.getVersion()).thenReturn("ver");
      when(uuidFactory.get()).thenReturn("uuid");

      target.checkTransmission(true);

      verify(client).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart())
          .isCloseTo(now(), byLessThan(1, SECONDS));
      assertThat(config.getSessionId())
          .isEqualTo("ver-uuid")
//          .startsWith("ver-")
//          .hasSize("ver-".length() + 36)
      ;
   }
}
