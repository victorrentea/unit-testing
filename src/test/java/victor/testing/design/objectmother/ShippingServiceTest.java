package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {

  @Test
  void estimateShippingCostsForRomania() {
    ShippingService shippingService = new ShippingService();
    Customer customer = TestData.aCustomer()
        .shippingAddress("Romania").build();

    int result = shippingService.estimateShippingCosts(customer);

    assertThat(result).isEqualTo(30);
  }
  @Test
  void estimateShippingCosts() {
    ShippingService shippingService = new ShippingService();
    Customer customer = TestData.aCustomer()
        .shippingAddress("India").build();

    int result = shippingService.estimateShippingCosts(customer);

    assertThat(result).isEqualTo(50);
  }
}