package victor.testing.design.creepymother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
   @Test
   void estimateShippingCosts() {
      Customer customer = TestData.aCustomer();

      int cost = new ShippingService().estimateShippingCosts(customer);

      assertThat(cost).isEqualTo(20);
   }
}
// ---------- CUT HERE AND BE HAPPY
class InvoiceServiceTest {
   @Test
   void invoice() {
      Customer customer = TestData.aCustomer()
              .setName("Jane")
              .setBillingAddress("BillingAddress");

      String invoice = new InvoiceService().generateInvoice(customer, "Order1");

      assertThat(invoice).isEqualTo("Invoice\n" +
                                    "Buyer: BillingAddress\n" +
                                    "For order Order1");
   }
}
// Object Mother
class TestData {
   public static Customer joe() { // next level: method name tells what the customer is "persona"
      return new Customer("Joe", "Romania", "BillingAddress");
   }


   // never change contents of an object mother, only ADD stuff.
   public static Customer aCustomer() { // <- method name tells that you only try to get past the validation
      return new Customer("Joe", "Romania", "BillingAddress");
   }
}