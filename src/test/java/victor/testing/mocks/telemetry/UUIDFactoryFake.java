package victor.testing.mocks.telemetry;

// for tests only
public class UUIDFactoryFake implements UUIDFactoryInterface{

  private final String uuid;

  public UUIDFactoryFake(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public String get() {
    return uuid;
  }
}
