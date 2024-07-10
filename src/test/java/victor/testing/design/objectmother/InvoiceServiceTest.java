package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceServiceTest {
  // WARNING: if you have setters on your class, DO NOT CREATE A BUILDER FOR IT. instead make setters fluent (returning this)
  @Test
  void generatesInvoiceWithValidCustomerAndOrder() {
    Customer customer = TestData.aCustomer().build();
    String order = "Order123";
    InvoiceService invoiceService = new InvoiceService();

    String invoice = invoiceService.generateInvoice(customer, order);

    assertThat(invoice).contains("Buyer name: John Doe");
    assertThat(invoice).contains("Address: 123 Main St");
    assertThat(invoice).contains("For order Order123");
  }

}