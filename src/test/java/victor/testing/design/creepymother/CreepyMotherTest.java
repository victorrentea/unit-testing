package victor.testing.design.creepymother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ShippingServiceTest {
   @Test
   void specialShippingCostsForRomania() {
      InvoicingCustomer invoicingCustomer = TestData.aCustomer()
              .setShippingAddress("Romania");

//      customer = mock(Customer.class);// Mocking/stubbing DATA OBJECT (entities/Dto carrying data)
//      when(customer.getShippingAddress()).thenReturn("Romania");
      // NEVER DO THAT.
      // mock behavior (faking the meth) <> dummy data (new with proper data)

      int cost = new ShippingService().estimateShippingCosts(invoicingCustomer);

      assertThat(cost).isEqualTo(20);
   }

}
class InvoiceServiceTest {
   @Test
   void invoice() {
      InvoicingCustomer invoicingCustomer = TestData.aCustomer()
              .setBillingAddress("##BillingAddress##");

      String invoice = new InvoiceService().generateInvoice(invoicingCustomer, "Order1");

      assertThat(invoice).isEqualTo("Invoice\n" +
                                    "Buyer: ##BillingAddress##\n" +
                                    "For order Order1");
   }
}
class TestData {
   public static InvoicingCustomer joe() {
      return new InvoicingCustomer("Joe", "Romania", "BillingAddress");
   }

   static InvoicingCustomer aCustomer() {
      return new InvoicingCustomer("Joe",
              "Shipping",
              "Billing");
   }
}