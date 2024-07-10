package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import victor.testing.design.objectmother.Customer.CustomerBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  // WARNING: if you have setters on your class, DO NOT CREATE A BUILDER FOR IT. instead make setters fluent (returning this)
  @Test
  void generatesInvoiceWithValidCustomerAndOrder() {
    Customer customer = validCustomer().build();
    String order = "Order123";
    InvoiceService invoiceService = new InvoiceService();

    String invoice = invoiceService.generateInvoice(customer, order);

    assertThat(invoice).contains("Buyer name: John Doe");
    assertThat(invoice).contains("Address: 123 Main St");
    assertThat(invoice).contains("For order Order123");
}

  private CustomerBuilder validCustomer() { // canned object
    return Customer.builder()
        .name("John Doe")
        .billingAddress("123 Main St")
        .shippingAddress("anything") // useless details that make the test harder to understand
        .phoneNumber("WHY?!!!");
  }
}