package victor.testing.design.creepymother;

import lombok.Data;

import static java.util.Objects.requireNonNull;

@Data
class InvoicingCustomer {
   private String name;
   private String shippingAddress;
//   @NotNull
   private String billingAddress;

   private int shoeSize;
   // 20 more fields...


//   protected Customer() {} // for Hibernate only
   InvoicingCustomer(String name, String shippingAddress, String billingAddress) {
      this.name = requireNonNull(name);
      this.shippingAddress = requireNonNull(shippingAddress);
      this.billingAddress = requireNonNull(billingAddress);
   }
   InvoicingCustomer(String name, String shippingAddress) {
      this.name = requireNonNull(name);
      this.shippingAddress = requireNonNull(shippingAddress);
//      this.billingAddress = "justh ere for testing. in prod. polluting the code.";
   }

   public int getShoeSize() {
      return shoeSize;
   }

   public InvoicingCustomer setShoeSize(int shoeSize) {
      this.shoeSize = shoeSize;
      return this;
   }

   public String getShippingAddress() {
      return shippingAddress;
   }

   public InvoicingCustomer setShippingAddress(String shippingAddress) {
      this.shippingAddress = shippingAddress;
      return this;
   }

}