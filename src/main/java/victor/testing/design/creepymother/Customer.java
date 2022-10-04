package victor.testing.design.creepymother;

import static java.util.Objects.requireNonNull;

class Customer {
   private String name;
   private String shippingAddress;
   private String billingAddress;
   // 20 more fields...

   Customer(String name, String shippingAddress, String billingAddress) {
      this.name = requireNonNull(name);
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