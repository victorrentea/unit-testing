package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.util.UUID;

import static java.time.LocalDateTime.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//@ExtendWith(MockitoExtension.class)
public class DiagnosticTest {
//   public static final InstantSource CHRISTMAS = InstantSource.fixed(parse("2023-12-25").toInstant(ZOffZONE_ID));
   //   @Mock
   UUIDFactoryInterface uuidFactory = new UUIDFactoryFake("uuid");
         //mock(UUIDFactory.class);
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
      UUID fixed = UUID.fromString("25571fb9-8967-4934-b949-2a8842d80108");
      when(client.getVersion()).thenReturn("ver");
//      when(uuidFactory.get()).thenReturn("uuid");

      // mockito-inline allows mocking statics(!), final and constructors: PowerMock or PowerMockito was required
      try (MockedStatic<UUID> staticMock = mockStatic(UUID.class)) {
         staticMock.when(UUID::randomUUID).thenReturn(fixed);
         target.checkTransmission(true);
      }
      verify(client).configure(configCaptor.capture());
      ClientConfiguration config = configCaptor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
      assertThat(config.getSessionStart())
          .isCloseTo(now(), byLessThan(1, SECONDS));
      assertThat(config.getSessionId())
          .isEqualTo("ver-25571fb9-8967-4934-b949-2a8842d80108")
//          .startsWith("ver-")
//          .hasSize("ver-".length() + 36)
      ;
   }

}
