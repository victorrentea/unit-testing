package victor.testing.design.objectmother;

class InvoiceService { //35k
   public String generateInvoice(Customer customer, String order) {
      String invoice = "Invoice\n";
      invoice += "Buyer name: " + customer.getName() + "\n";
      invoice += "Address: " + customer.getBillingAddress() + "\n";
      invoice += "For order " + order ;
      // more
      return invoice;
   }
}