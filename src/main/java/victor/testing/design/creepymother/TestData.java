package victor.testing.design.creepymother;

import static java.util.Objects.requireNonNull;

class ShippingService {
   public int estimateShippingCosts(Customer customer) {
      if (!customer.getShippingAddress().contains("Romania")) {
         return 50;
      }
      // more logic
      return 20;
   }
}
class InvoiceService {
   public String generateInvoice(Customer customer, String order) {
      String invoice = "Invoice\n";
      invoice += "Buyer: " + customer.getBillingAddress() + "\n";
      invoice += "For order " + order ;
      // more
      return invoice;
   }
}

class Customer {
   private String shippingAddress;
   private String billingAddress;

   Customer(String shippingAddress, String billingAddress) {
      this.shippingAddress = requireNonNull(shippingAddress);
      this.billingAddress = requireNonNull(billingAddress);
   }

   public String getShippingAddress() {
      return shippingAddress;
   }

   public Customer setShippingAddress(String shippingAddress) {
      this.shippingAddress = shippingAddress;
      return this;
   }

   public String getBillingAddress() {
      return billingAddress;
   }

   public Customer setBillingAddress(String billingAddress) {
      this.billingAddress = billingAddress;
      return this;
   }
}