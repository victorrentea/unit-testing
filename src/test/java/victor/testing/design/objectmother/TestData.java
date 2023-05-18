package victor.testing.design.objectmother;

public class TestData {
  public static Customer.CustomerBuilder joe() {
    return Customer.builder()
        .name("Joe")
        .shippingAddress("Romania")
        .billingAddress("BillingAddress");
  }
}
