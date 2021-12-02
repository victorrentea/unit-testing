package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // @RunWith(MockitoJUnitRunner)
public class TelemetryDiagnosticTest {
   @Mock
   private TelemetryClient clientMock;
   @InjectMocks
   private TelemetryDiagnostic target;
   @BeforeEach
   final void before() {
      when(clientMock.getOnlineStatus()).thenReturn(true); // useful for the majority of @Test below
   }
   
   @Test
   void disconnects() {
//      Mockito.doReturn("aaa").when(clientMock.getOnlineStatus());  // NEVER use this
      // doReturn is only used when using @Spy (partial mocks) which are ANTI-PATTERNS

      target.checkTransmission(true);

      verify(clientMock).disconnect(true);
   }

   @Test
   void throwsWhenNotOnline() {
      // given
      when(clientMock.getOnlineStatus()).thenReturn(false); // overrides the stubbed behavior from beforeEach

      //when
      assertThatThrownBy(() -> target.checkTransmission(true));
   }

   @Test
   void send() {

      target.checkTransmission(true);

      verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE); // most readable < pick this usually
      verify(clientMock).send("AT#UD"); // prefer this when the data goes to an external system (is part of a protocl)
      verify(clientMock).send(anyString()); // weakest
   }
   
   @Test
   void receives() {
      // given
      when(clientMock.receive()).thenReturn("::receivedData::");

      // when
      target.checkTransmission(true);

      //then
      verify(clientMock).receive();  //became usless when I checked what happened with the returned value  (next line)
      // verify of a method that you did when.thenReturn is acceptable only if
      // 1) it has significant Side effects (eg INSERT) - means the method is violating the CQS principle
      // 2) performance: if it's a HTTP/SOAP network call, you want to make sure it happens just once

      assertThat(target.getDiagnosticInfo()).isEqualTo("::receivedData::");
   }
//      verify(clientMock, never()).paidCall(); // $
//      verify(clientMock, never()).reportBadLoanPayer(customerId); // critical damaging side effect

   @Test
   void test() {
      // given
      ArgumentCaptor<ClientConfiguration> captor = forClass(ClientConfiguration.class);

      //when - prod call
      target.checkTransmission(true);

      // then
      verify(clientMock).configure(captor.capture());
      ClientConfiguration config = captor.getValue();
      assertThat(config.getAckMode()).isEqualTo(AckMode.NORMAL);
   }

}
