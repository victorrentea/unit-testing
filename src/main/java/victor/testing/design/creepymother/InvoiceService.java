package victor.testing.design.creepymother;

class InvoiceService {
   public String generateInvoice(Customer customer, String order) {
      String invoice = "Invoice\n";
      invoice += "Buyer name: " + customer.getName() + "\n";
      invoice += "Address: " + customer.getBillingAddress() + "\n";
      invoice += "For order " + order ;
      // more
      return invoice;
   }
}