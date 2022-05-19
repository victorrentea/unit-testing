package victor.testing.designhints.creepymother;


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

//
//class BillingCustomer {
//   private String billingAddress;
//
//   Customer(String shippingAddress, String billingAddress) {
//      this.billingAddress = requireNonNull(billingAddress);
//   }
//
//   public String getBillingAddress() {
//      return billingAddress;
//   }
//
//   public victor.testing.mutation.Customer setBillingAddress(String billingAddress) {
//      this.billingAddress = billingAddress;
//      return this;
//   }
//}
//
//class ShippingCustomer {
//   private String shippingAddress;
//
//   ShippingCustomer(String shippingAddress, String billingAddress) {
//      this.shippingAddress = requireNonNull(shippingAddress);
//   }
//
//   public String getShippingAddress() {
//      return shippingAddress;
//   }
//
//   public victor.testing.mutation.Customer setShippingAddress(String shippingAddress) {
//      this.shippingAddress = shippingAddress;
//      return this;
//   }
//}


class Customer {
//   private ShippingCustomer
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