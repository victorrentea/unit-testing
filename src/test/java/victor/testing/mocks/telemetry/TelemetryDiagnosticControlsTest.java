package victor.testing.mocks.telemetry;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class TelemetryDiagnosticControlsTest {
	@Mock
	private TelemetryClient clientMock;
	@Mock
	private ClientConfigurationFactory configFactoryMock;
	@InjectMocks
	private TelemetryDiagnosticControls controls;

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
		ClientConfiguration config = new ClientConfiguration();
		when(clientMock.getVersion()).thenReturn("ver");
		when(configFactoryMock.createConfig("ver")).thenReturn(config);
		controls.checkTransmission();
		verify(clientMock).configure(config); // inginereste vorbind e suficient
		
		// cum pot verifica daca s-a chemat functia createConfig() 
		// problema e ca functia e locala, in aceeasi clasa pecare o si stestez
		// nu e pe un mock injectat si direct in clasa testata.
		//
	}
	
	
	
}
