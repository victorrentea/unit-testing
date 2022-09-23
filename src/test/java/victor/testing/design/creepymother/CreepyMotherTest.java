package victor.testing.design.creepymother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {

   @Test
   void estimateShippingCosts() {
      Customer customer = new Customer("Romania", "whatever");

      int cost = new ShippingService().estimateShippingCosts(customer);

      assertThat(cost).isEqualTo(20);
   }
}
class InvoiceServiceTest {
   @Test
   void invoice() {
      Customer customer = new Customer("whatever", "BillingAddress");

      String invoice = new InvoiceService().generateInvoice(customer, "Order1");

      assertThat(invoice).isEqualTo("Invoice\n" +
                                    "Buyer: BillingAddress\n" +
                                    "For order Order1");
   }
}