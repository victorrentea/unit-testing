package victor.testing.design.objectmother;

class ShippingService { // 10k linii
   public int estimateShippingCosts(Customer customer) {
      if (customer.getShippingAddress().contains("Romania")) {
         return 30;
      }
      // more logic
      return 50;
   }

   public String printShippingSlip(Customer customer) {
      return "Recipient name: " + customer.getName() +
             "\nAddress: " + customer.getShippingAddress();
   }
}