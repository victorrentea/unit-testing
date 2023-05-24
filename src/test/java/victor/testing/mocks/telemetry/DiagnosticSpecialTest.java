package victor.testing.mocks.telemetry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DiagnosticSpecialTest {

	Diagnostic diagnostic = new Diagnostic();
	Client client = Mockito.mock(Client.class);

	@BeforeEach
	void innit() {
		diagnostic.setTelemetryClient(client);
	}

	@Test
	void emptyDiagnosticInitially() {
		assertThat(diagnostic.getDiagnosticInfo()).isEqualTo("");
	}

	@Test
	void throwWhenUnableToConnect() {
		doReturn(false).when(client).getOnlineStatus();

		assertThatThrownBy(() -> diagnostic.checkTransmission(true))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("Unable to connect.");
	}

	@Test
	void shouldRetryThreeTimes() {
		when(client.getOnlineStatus())
				.thenReturn(false, false, false, true);
		diagnostic.checkTransmission(false);
		verify(client, Mockito.times(3)).connect(anyString());
	}

	@Test
	void receiveInfoFromClient() {
		String message = "Something simple";
		when(client.receive()).thenReturn(message);
		when(client.getOnlineStatus()).thenReturn(true);
		diagnostic.checkTransmission(false);

		assertThat(diagnostic.getDiagnosticInfo()).isEqualTo(message);
		verify(client, atLeastOnce()).send(anyString());
		verify(client, times(1)).configure(any(Client.ClientConfiguration.class));
	}

	@Test
	@DisplayName("コードを書く ジャバは強力だ オブジェクト指向")
	void shouldContainVersionInSession() {
		String wisdom = "コードを書く ジャバは強力だ オブジェクト指向";
		when(client.getVersion()).thenReturn(wisdom);
		when(client.getOnlineStatus()).thenReturn(true);
		MockedConstruction<Client.ClientConfiguration> mock = mockConstruction(Client.ClientConfiguration.class);
		diagnostic.checkTransmission(true);
		List<Client.ClientConfiguration> createdConfigurations = mock.constructed();
		Client.ClientConfiguration mockedConfiguration = createdConfigurations.get(0);
		assertThat(createdConfigurations.size()).isEqualTo(1);
		verify(mockedConfiguration).setSessionId(matches(wisdom + ".*"));
		verify(mockedConfiguration, times(1)).setAckMode(any(Client.ClientConfiguration.AckMode.class));
		verify(mockedConfiguration, times(1)).setSessionStart(any(LocalDateTime.class));
	}

}
