package victor.testing.design.creepymother;

class ShippingService {
   public int estimateShippingCosts(Customer customer) {
      if (!customer.getShippingAddress().contains("Romania")) {
         return 50;
      }
      // more logic
      return 20;
   }

   public String printShippingSlip(Customer customer) {
      return "Recipient name: " + customer.getName() +
             "\nAddress: " + customer.getShippingAddress();
   }
}