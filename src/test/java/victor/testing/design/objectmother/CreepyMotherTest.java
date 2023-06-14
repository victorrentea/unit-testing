package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  // ??? = unnecessary information

  @Test
  void estimateShippingCostsRo() {
    Customer customer = ShippingTestData.joe().setShippingAddress("Romania");
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(30);
  }
  @Test
  void estimateShippingCosts() {
    Customer customer = ShippingTestData.joe().setShippingAddress("Germany");
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(50);
  }

  @Test
  void printShippingSlip() {
    Customer customer = ShippingTestData.joe()
        .setName("Joe")
        .setShippingAddress("Romania");
    String shippingSlip = new ShippingService().printShippingSlip(customer);
    assertThat(shippingSlip).isEqualTo("""
            Recipient name: Joe
            Address: Romania""");
  }
}

class InvoiceServiceTest {
  @Test
  void invoice() {
    Customer customer = InvoiceTestData.joe()
        .setName("Mr Bean")
        .setBillingAddress("BillingAddress");
    String invoice = new InvoiceService().generateInvoice(customer, "Order1");
    assertThat(invoice).isEqualTo("""
            Invoice
            Buyer name: Mr Bean
            Address: BillingAddress
            For order Order1""");
  }
}

class ShippingTestData {
  // standard objects to derive from in various tests
  public static Customer joe() { // many tests end up depending on this
    return new Customer()
            .setName("Joe")
            .setEmail("jdoe@example.com")
            .setShippingAddress("Romania");
  }
}
class InvoiceTestData {
  // standard objects to derive from in various tests
  public static Customer joe() { // many tests end up depending on this
    return new Customer()
            .setName("Joe")
            .setEmail("jdoe@example.com")
            .setBillingAddress("BillingAddress");
  }
}