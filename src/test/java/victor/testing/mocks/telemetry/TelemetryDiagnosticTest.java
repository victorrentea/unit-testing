package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//class Fake implements I {}
@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticTest {
    @Mock
    TelemetryClient clientMock; // = mock(TelemetryClient.class);
    @InjectMocks
    TelemetryDiagnostic diagnostic;// = new TelemetryDiagnostic(mock);



    @Test
    void disconnects() {
        when(clientMock.getOnlineStatus()).thenReturn(true);

        // prod call
        diagnostic.checkTransmission(true);

        verify(clientMock).disconnect(true);
    }
    @Test
    void throwsWhenNotOnline() {
        when(clientMock.getOnlineStatus()).thenReturn(false);

        assertThatThrownBy(() ->diagnostic.checkTransmission(true))
                .isInstanceOf(IllegalStateException.class);

    }
}
