package victor.testing.mocks.telemetry;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

public class Client {
	public static final String DIAGNOSTIC_MESSAGE = "AT#UD";


	public static class ClientConfiguration {
		enum AckMode {NORMAL, TIMEBOXED, FLOOD};
		private String sessionId;
		private LocalDateTime sessionStart;
		private AckMode ackMode;
		
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
		public LocalDateTime getSessionStart() {
			return sessionStart;
		}
		public void setSessionStart(LocalDateTime sessionStart) {
			this.sessionStart = sessionStart;
		}
		public AckMode getAckMode() {
			return ackMode;
		}
		public void setAckMode(AckMode ackMode) {
			this.ackMode = ackMode;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ClientConfiguration that = (ClientConfiguration) o;
			return Objects.equals(sessionId, that.sessionId) && Objects.equals(sessionStart, that.sessionStart) && ackMode == that.ackMode;
		}

		@Override
		public int hashCode() {
			return Objects.hash(sessionId, sessionStart, ackMode);
		}
	}

	private boolean onlineStatus;
	private String diagnosticMessageResult = "";

	private final Random connectionEventsSimulator = new Random(42);

	public boolean getOnlineStatus() {
		return onlineStatus;
	}

	public void connect(String telemetryServerConnectionString) {
		if (telemetryServerConnectionString == null || "".equals(telemetryServerConnectionString)) {
			throw new IllegalArgumentException();
		}

		// simulate the operation on a real modem
		boolean success = connectionEventsSimulator.nextInt(10) <= 8;

		onlineStatus = success;
	}

	public String getVersion() {
		return "1.3";
	}

	public void disconnect(boolean force) {
		onlineStatus = false;
		throw new IllegalStateException("Performs some external remote call impossible/not desirable to call from automated tests.");
	}

	public void send(String message) {
		if (message == null || "".equals(message)) {
			throw new IllegalArgumentException();
		}

		if (message == DIAGNOSTIC_MESSAGE) {
			// simulate a status report
			diagnosticMessageResult = "LAST TX rate................ 100 MBPS\r\n"
					+ "HIGHEST TX rate............. 100 MBPS\r\n" + "LAST RX rate................ 100 MBPS\r\n"
					+ "HIGHEST RX rate............. 100 MBPS\r\n" + "BIT RATE.................... 100000000\r\n"
					+ "WORD LEN.................... 16\r\n" + "WORD/FRAME.................. 511\r\n"
					+ "BITS/FRAME.................. 8192\r\n" + "MODULATION TYPE............. PCM/FM\r\n"
					+ "TX Digital Los.............. 0.75\r\n" + "RX Digital Los.............. 0.10\r\n"
					+ "BEP Test.................... -5\r\n" + "Local Rtrn Count............ 00\r\n"
					+ "Remote Rtrn Count........... 00";

			return;
		}

		// here should go the real Send operation (not needed for this exercise)
	}

	public String receive() {
		String message;

		if (diagnosticMessageResult == null || "".equals(diagnosticMessageResult)) {
			// simulate a received message (just for illustration - not needed for this exercise)
			message = "";
			int messageLength = connectionEventsSimulator.nextInt(50) + 60;
			for (int i = messageLength; i >= 0; --i) {
				message += (char) connectionEventsSimulator.nextInt(40) + 86;
			}

		} else {
			message = diagnosticMessageResult;
			diagnosticMessageResult = "";
		}

		return message;
	}

	public void configure(ClientConfiguration config) {
		 //TODO Configure the bank
	}
}
