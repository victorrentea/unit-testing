package victor.testing.design.creepymother;

class InvoiceService {
   public String generateInvoice(InvoicingCustomer invoicingCustomer, String order) {
      String invoice = "Invoice\n";
      invoice += "Buyer: " + invoicingCustomer.getBillingAddress() + "\n";
      invoice += "For order " + order ;
      // more
      return invoice;
   }
}