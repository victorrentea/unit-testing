package victor.testing.mutation;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class TestData {

  public static  Customer aValidCustomer() { // in P returnezi un Builder
    Customer customer = new Customer();
    customer.setName("Joe");
    customer.setEmail("::email::");
    customer.setAddress(new Address());
    customer.getAddress().setCity("::city::");
    return customer;
  }

  static ObjectMapper objectMapper = new ObjectMapper();

//  private static Player player;
//
//  public static Player getPlayer() {
//    return player;
//  }
//
//  static {
//    player = objectMapper.readValue("{}", Player.class);
//
//  }
}
