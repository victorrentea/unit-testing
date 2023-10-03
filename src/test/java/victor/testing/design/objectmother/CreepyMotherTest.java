package victor.testing.design.objectmother;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingServiceTest {
  // ??? = unnecessary information
  @Test
  void estimateShippingCosts() {
    Customer customer = new Customer("???", "Belgium", "???");
    int cost = new ShippingService().estimateShippingCosts(customer);
    assertThat(cost).isEqualTo(30);
  }

  @Test
  void printShippingSlip() {
    Customer customer = new Customer("Joe", "Belgium", "???");
    String shippingSlip = new ShippingService().printShippingSlip(customer);
    assertThat(shippingSlip).isEqualTo("""
            Recipient name: Joe
            Address: Belgium""");
  }
}

class InvoicingServiceTest {
  @Test
  void invoice() {
    Customer customer = new Customer("Mr Bean", "???", "BillingAddress");
    String invoice = new InvoicingService().generateInvoice(customer, "Order1");
    assertThat(invoice).isEqualTo("""
            Invoice
            Buyer name: Mr Bean
            Address: BillingAddress
            For order Order1""");
  }
}

class TestData {
  public static Customer.CustomerBuilder joe() {
    return Customer.builder()
            .name("Joe")
            .shippingAddress("Belgium")
            .billingAddress("BillingAddress");
  }


  public static Customer.CustomerBuilder joeJson() {
    try (InputStream is = TestData.class.getClassLoader().getResourceAsStream("customer.json")) {
      Customer customer = new ObjectMapper().readValue(is, Customer.class);
      return customer.toBuilder();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws JsonProcessingException {
//    System.out.println(new ObjectMapper().writeValueAsString(joe().build()));
    System.out.println(joeJson());
  }
}