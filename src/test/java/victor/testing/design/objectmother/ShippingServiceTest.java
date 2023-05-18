package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShippingServiceTest {
  // ??? = unnecessary information
  @Test
  void estimateShippingCosts() {
    Customer customer = TestData.joe().shippingAddress("Romania").build();
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(30);
  }
  @Test
  void printShippingSlip() {
    Customer customer = TestData.joe().shippingAddress("Romania").build();
    String shippingSlip = new ShippingService().printShippingSlip(customer);
    assertThat(shippingSlip).isEqualTo("""
        Recipient name: Joe
        Address: Romania""");
  }
}
