package victor.testing.mocks.telemetry;

public interface ITelemetryDiagnostic {
   String getDiagnosticInfo();

   void checkTransmission(boolean force);
}
