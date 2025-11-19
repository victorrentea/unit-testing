package victor.testing.design.objectmother;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  // ??? = unnecessary information
  @Test
  void estimateShippingCosts() {
    Customer customer = aCustomer();
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(30);
  }

  private Customer aCustomer() {
    return new Customer(
        "???",
        "Romania", "???");
  }

  @Test
  void printShippingSlip() {
    Customer customer = new Customer("Joe", "Romania", "???");
    String shippingSlip = new ShippingService().printShippingSlip(customer);
    assertThat(shippingSlip).isEqualTo("""
            Recipient name: Joe
            Address: Romania""");
  }
}

class InvoiceServiceTest {
  @Test
  void invoice() {
    Customer customer = new Customer("Mr Bean", "???", "BillingAddress");
    String invoice = new InvoiceService().generateInvoice(customer, "Order1");
    assertThat(invoice).isEqualTo("""
            Invoice
            Buyer name: Mr Bean
            Address: BillingAddress
            For order Order1""");
  }
}

class TestData {
  public static Customer.CustomerBuilder joe() {
    return Customer.builder()
            .name("Joe")
            .shippingAddress("Romania")
            .billingAddress("BillingAddress");
  }
}