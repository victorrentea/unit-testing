package victor.testing.mocks.telemetry;

import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class UUIDGenerator {
  public String uuid() {
    return randomUUID().toString();
  }
}
