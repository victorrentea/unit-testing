package victor.testing.design.objectmother;

class ShippingService {
   public int estimateShippingCosts(Customer customer) {
      if (customer.getShippingAddress().contains("Belgium")) {
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