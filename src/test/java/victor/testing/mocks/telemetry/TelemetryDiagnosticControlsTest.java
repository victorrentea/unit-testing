package victor.testing.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TelemetryDiagnosticControlsTest {
	private TelemetryClient clientMock = mock(TelemetryClient.class);
	private TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(clientMock);
	@Test
	public void disconnects() throws Exception {
		when(clientMock.getOnlineStatus()).thenReturn(true);
		
		controls.checkTransmission();
		
		verify(clientMock).disconnect();
	}
	@Test(expected = IllegalStateException.class)
	public void throwsWhenNotOnline() throws Exception {
//		when(clientMock.getOnlineStatus()).thenReturn(false);
		
		controls.checkTransmission();
	}
	@Test
	public void sendsDiagnosticMessage() throws Exception {
		when(clientMock.getOnlineStatus()).thenReturn(true);
		
		controls.checkTransmission();
		
		verify(clientMock).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
	}
}
