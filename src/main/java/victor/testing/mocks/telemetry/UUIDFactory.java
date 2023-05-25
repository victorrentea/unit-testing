package victor.testing.mocks.telemetry;

import java.util.UUID;

public class UUIDFactory implements UUIDFactoryInterface {
  @Override
  public String get() {
    return UUID.randomUUID().toString();
  }
}
