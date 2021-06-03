package victor.testing.mocks.telemetry;

import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration;
import victor.testing.mocks.telemetry.TelemetryClient.ClientConfiguration.AckMode;

import java.time.LocalDateTime;
import java.util.UUID;

public class TelemetryDiagnosticControls {
	public static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

	private final TelemetryClient telemetryClient;
	private final ClientConfigurationFactory configurationFactory;
	private String diagnosticInfo = "";

	public TelemetryDiagnosticControls(TelemetryClient telemetryClient, ClientConfigurationFactory configurationFactory) {
		this.telemetryClient = telemetryClient;

		this.configurationFactory = configurationFactory;
	}


	public String getDiagnosticInfo() {
		return diagnosticInfo;
	}
	public void setDiagnosticInfo(String diagnosticInfo) {
		this.diagnosticInfo = diagnosticInfo;
	}

//	@Timed //(micrometer)
	public void checkTransmission(boolean force) {
		telemetryClient.disconnect(force);

		int currentRetry = 1;
		while (! telemetryClient.getOnlineStatus() && currentRetry <= 3) {
			telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
			currentRetry ++;
		}

		if (! telemetryClient.getOnlineStatus()) {
			throw new IllegalStateException("Unable to connect.");
//			throw new MyException(ErrorCodeEnum.CANNOT_CONNECT);
		}

		// de aici
		ClientConfiguration config = configurationFactory.createConfig(telemetryClient.getVersion());
		telemetryClient.configure(config);
		// pana aici

		telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
		diagnosticInfo = telemetryClient.receive();
	}

}
class ClientConfigurationFactory {

	public ClientConfiguration createConfig(String version) { // , , ,  , , == >cand sunt multe -> ValueObject
		ClientConfiguration config = new ClientConfiguration();
		config.setSessionId(version.toUpperCase() + "-" + UUID.randomUUID().toString());
		// MULTA LOGICA AICI
		config.setSessionStart(LocalDateTime.now());
		config.setAckMode(AckMode.NORMAL); // So! pa ea
		return config;

	}

}
// Agregate DDD
//@Configurable
////@Entity
//class PoliticalPersons {
//	List<Person> millions;
//	@Autowired
//	private PersonRepo repo;
//@Basic(LAZY)
//
//	public List<Person> getTop30Millions() {
//		return repo.findAllAsStream();
//	}
//	@PreAuthorize("hasRole(ADMIN)")
//	void addPerson(Person person) {}
//}
//
//class ServiceX {
//	@Autowired
//	ServiceX myself;
//	public void computeWhateverTotal() {
//		myself.localMethod();// bleah
//	}
//
//	@Transactional(propagation = Propagation.REQUIRES_NEW) // nu merge nicioadata
//	private void localMethod() {
//
//	}
//}
