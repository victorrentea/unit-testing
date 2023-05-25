package victor.testing.mocks.telemetry;

import java.util.UUID;

public class UUIDFactory {
  public String get() {
    return UUID.randomUUID().toString();
  }
}
