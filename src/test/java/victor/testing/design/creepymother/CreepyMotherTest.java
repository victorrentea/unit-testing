package victor.testing.design.creepymother;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  // ??? = unnecessary information
  @Test
  void estimateShippingCosts() {
//    Customer customer = new Customer("???",
//            "Romania", "???");
    Customer customer = ShippingTestData.marcel().build();
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(20);
  }
  @Test
  void estimateShippingCostsNonRO() {
    Customer customer = ShippingTestData.marcel().shippingAddress("Altceva").build();
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(50);
  }

  @Test
  void printShippingSlip() {
//    Customer customer = new Customer("Joe", "Romania", "???");
    Customer customer = ShippingTestData.marcel().build();
    String shippingSlip = new ShippingService().printShippingSlip(customer);
    assertThat(shippingSlip).isEqualTo("""
            Recipient name: Marcel
            Address: Romania""");
  }
}

class InvoiceServiceTest {
  @Test
  void invoice() {
//    Customer customer = new Customer("Mr Bean", "???", "BillingAddress");
    Customer customer = BillingTestData.marcel().name("Mr Bean").build();
    String invoice = new InvoiceService().generateInvoice(customer, "Order1");
    assertThat(invoice).isEqualTo("""
            Invoice
            Buyer name: Mr Bean
            Address: BillingAddress
            For order Order1""");
  }
}
  // in met din object mother NU AI VOIE SA MODIFICI NIMIC
  // poti 1) adaugi metode noi
  // 2) customizezi obiectul returnat
// aka Object Mother
class ShippingTestData {
  public static Customer.CustomerBuilder marcel() {
    return Customer.builder()
            .name("Marcel")
            .shippingAddress("Romania");
  }
}
class BillingTestData {
  // in met din object mother NU AI VOIE SA MODIFICI NIMIC
  // poti 1) adaugi metode noi
  // 2) customizezi obiectul returnat
  public static Customer.CustomerBuilder marcel() {
    return Customer.builder()
            .name("Marcel")
            .billingAddress("BillingAddress");
  }
}