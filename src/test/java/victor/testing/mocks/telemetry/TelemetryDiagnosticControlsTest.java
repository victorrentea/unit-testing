package victor.testing.mocks.telemetry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TelemetryDiagnosticControlsTest {
	@Test
	public void testName() throws Exception {
		TelemetryClient client = mock(TelemetryClient.class);
		when(client.getOnlineStatus()).thenReturn(true);
		
		TelemetryDiagnosticControls controls = new TelemetryDiagnosticControls(client);
		
		controls.checkTransmission();
	}
}
