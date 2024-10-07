package victor.testing.mutation;

import lombok.SneakyThrows;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class TestData {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @SneakyThrows
  public static Customer aCustomerJson() {
    return objectMapper.readValue(TestData.class.getResource("/canonical/customer.json"), Customer.class);
  }
}
