package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.Client.ClientConfiguration;
import victor.testing.mocks.telemetry.Client.ClientConfiguration.AckMode;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;

public class Diagnostic {
  public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

  private Client client;
  private String diagnosticInfo = "";

  public void setTelemetryClient(Client client) {
    this.client = client;
  }

  public void checkTransmission(boolean force) {
    client.disconnect(force);

    int currentRetry = 1;
    while (!client.getOnlineStatus() && currentRetry <= 3) {
      client.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
      currentRetry++;
    }

    if (!client.getOnlineStatus()) {
      throw new IllegalStateException("Unable to connect.");
    }

    configureClient(force);

    client.send(Client.DIAGNOSTIC_MESSAGE);
    diagnosticInfo = client.receive();
//    diagnosticInfo = client.receive();
//    diagnosticInfo = client.receive();
//    diagnosticInfo = client.receive();
  }

  void configureClient(boolean force) {
    ClientConfiguration config = new ClientConfiguration();
    config.setSessionId(client.getVersion().toUpperCase() + "-" + randomUUID());
    // + 7 ifuri
	  //		if () {thr}
	  //		if () {}
	  //		if () {}
	  //		if () {}
	  //		if () {}
	  //		if () {}
	  //		if () {}
	  //		if () {}
    config.setSessionStart(LocalDateTime.now());
    if (force) {
      config.setAckMode(AckMode.NORMAL);
    } else {
      config.setAckMode(AckMode.TIMEBOXED);
    }
    client.configure(config);
  }

  public String getDiagnosticInfo() {
    return diagnosticInfo;
  }

}










