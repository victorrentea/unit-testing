package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  void generatesInvoiceWithValidCustomerAndOrder() {
    Customer customer = new Customer(
        "John Doe",
        "123 Main St",
        "123 Main St",
        "WHY?!!!");
    String order = "Order123";

    InvoiceService invoiceService = new InvoiceService();
    String invoice = invoiceService.generateInvoice(customer, order);

    assertThat(invoice).contains("Buyer name: John Doe");
    assertThat(invoice).contains("Address: 123 Main St");
    assertThat(invoice).contains("For order Order123");
}
}