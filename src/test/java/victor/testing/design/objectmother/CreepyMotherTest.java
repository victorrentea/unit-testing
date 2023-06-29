package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  // ??? = unnecessary information
  @Test
  void estimateShippingCosts() {
    Customer customer = TestData.joe()
        .shippingAddress("Some address in Romania")
        .build();
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(30);
  }

  @Test
  void printShippingSlip() {
//    Customer customer = new Customer("Joe", "Romania", "???");
    Customer customer = TestData.joe().shippingAddress("Romania").build();
    String shippingSlip = new ShippingService().printShippingSlip(customer);
    assertThat(shippingSlip).isEqualTo("""
            Recipient name: Joe
            Address: Romania""");
  }
}

class InvoiceServiceTest {
  @Test
  void invoice() {
//    Customer customer = new Customer("Mr Bean", "???", "BillingAddress");
    Customer customer = TestData.joe().name("Mr Bean").billingAddress("BillingAddress").build();

    String invoice = new InvoiceService().generateInvoice(customer, "Order1");
    assertThat(invoice).isEqualTo("""
            Invoice
            Buyer name: Mr Bean
            Address: BillingAddress
            For order Order1""");
  }
}

class TestData {
  // If Customer would be a mutable class (eg an @Entity) -> you don't need @Builder, you can make the lombok-generated setters return "this"
  // If Customer @Document is immutable ❤️ -> Builders start to make sense
  public static Customer.CustomerBuilder joe() {
    return Customer.builder()
            .name("Joe")
            .shippingAddress("Romania")
            .billingAddress("BillingAddress");
  }
}