package victor.testing.mocks.telemetry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.common.protocol.types.Field.UUID;


public class TelemetryDiagnosticControlsTest {
	private TelemetryClient clientMock = mock(TelemetryClient.class);
	private TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(clientMock);

	@Before
	public void initialize() {
		when(clientMock.getOnlineStatus()).thenReturn(true);
	}
	
	@Test
	public void disconnects() throws Exception {
		controls.checkTransmission();
		
		verify(clientMock).disconnect();
	}
	@Test(expected = IllegalStateException.class)
	public void throwsWhenNotOnline() throws Exception {
		when(clientMock.getOnlineStatus()).thenReturn(false);
		
		controls.checkTransmission();
	}
	@Test
	public void sendsDiagnosticMessage() throws Exception {
		controls.checkTransmission();
		
		// mai human friendly e contanta (are nume)
		verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		
		// CAND pui literalul?: 
//		verify(clientMock).send("AT#UD"); // un protocol de comm cu un ext service (chiar HARDWARE)
	}
	@Test
	public void receives() throws Exception {
		when(clientMock.receive()).thenReturn("mamaie");
		
		controls.checkTransmission();
		
		// e util sa .verify ceva ce ai when.then-uit DOAR daca iti pasa de cate ori cheama functia resp
		// cand iti pasa?: performanta (timp)
//		verify(clientMock).receive();
		assertEquals("mamaie", controls.getDiagnosticInfo());
	}
	
	@Test
	public void configuresClient() throws Exception {

		ClientConfiguration config = controls.createConfig();
		
		assertEquals(AckMode.NORMAL, config.getAckMode());
	}
	
	
	
}
