package victor.testing.tdd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class CumCompar2ObiecteCaSuntIdenticeCampCuCamp {

   @Test
   public void test() throws JsonProcessingException {
      Customer c1 = new Customer("John", new Address("Dristorului", "Bucuresti"), 1);
      Customer c2 = new Customer("John", new Address("Dristorului", "Bucuresti"), 1);

      String c1s = new ObjectMapper().writeValueAsString(c1);
      String c2s = new ObjectMapper().writeValueAsString(c2);

      Assert.assertEquals(c2s, c1s);
   }
}

@Data
@AllArgsConstructor
class Customer {
 private  String name;
   private  Address address;
   private int ceva;
}
@AllArgsConstructor
@Data
class Address {
   private String streetName;
   private  String city;
}