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

  @Test
  void printShippingSlipWithValidCustomer() {
    ShippingService shippingService = new ShippingService();
    Customer customer = TestData.aCustomer()
//        .name("Jane Doe")
        .shippingAddress("765 Main St, Romania")
        .build();

    String result = shippingService.printShippingSlip(customer);

    assertThat(result)
        .contains("Recipient name: Jane Doe")
        .contains("Address: 765 Main St, Romania");
  }
}