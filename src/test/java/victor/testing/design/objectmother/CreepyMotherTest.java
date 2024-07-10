package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  @Test
  void generatesInvoiceWithValidCustomerAndOrder() {
//    Customer customer = Customer.builder()
//        .name("John Doe")
//        .billingAddress("123 Main St")
//        .shippingAddress("123 Main St")
//        .phoneNumber("WHY?!!!")
//        .build();
    Customer customer = Mockito.mock(Customer.class);
    Mockito.when(customer.getName()).thenReturn("John Doe");
    Mockito.when(customer.getBillingAddress()).thenReturn("123 Main St");
    String order = "Order123";

    InvoiceService invoiceService = new InvoiceService();
    String invoice = invoiceService.generateInvoice(customer, order);

    assertThat(invoice).contains("Buyer name: John Doe");
    assertThat(invoice).contains("Address: 123 Main St");
    assertThat(invoice).contains("For order Order123");
}
}