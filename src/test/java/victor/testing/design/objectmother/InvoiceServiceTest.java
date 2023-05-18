package victor.testing.design.objectmother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceServiceTest {
  @Test
  void invoice() {
    Customer customer = TestData.joe()
        .name("Mr Bean")
        .billingAddress("BillingAddress")
        .build();
    String invoice = new InvoiceService().generateInvoice(customer, "Order1");
    assertThat(invoice).isEqualTo("""
            Invoice
            Buyer name: Mr Bean
            Address: BillingAddress
            For order Order1""");
  }
}

