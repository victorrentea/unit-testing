package victor.testing.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UUIDExtensionTest {

  public static final String UUID1 = "4e817bdb-66ac-4187-9ac6-5c0e25b68e9f";
  public static final String UUID2 = "29b63d5c-77df-475a-b9d1-4227c770e711";

  @RegisterExtension
  UUIDExtension uuidExtension = new UUIDExtension(UUID1);

  @Test
  void returnsSame() {
    // in prod
    UUID uuid = UUID.randomUUID();

    assertThat(uuid.toString()).isEqualTo(UUID1);
  }

  @Test
  void returnsSame_repeat() {
    assertThat(UUID.randomUUID().toString()).isEqualTo(UUID1);
    assertThat(UUID.randomUUID().toString()).isEqualTo(UUID1);
  }

  @Test
  void returnsSequence() {
    uuidExtension.add(UUID2);

    assertThat(UUID.randomUUID().toString()).isEqualTo(UUID1);
    assertThat(UUID.randomUUID().toString()).isEqualTo(UUID2);
  }

}
