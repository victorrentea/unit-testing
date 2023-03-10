package victor.testing.design.creepymother;

class ShippingService {
   public int estimateShippingCosts(InvoicingCustomer invoicingCustomer) {
      if (!invoicingCustomer.getShippingAddress().contains("Romania")) {
         return 50;
      }
      // more logic
      return 20;
   }
}