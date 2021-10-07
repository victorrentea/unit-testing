package victor.testing.builder;

// Object Mother
public class TestData {
   public static Customer aCustomer() {
      // setteri fluenti generati de Lombok cu lombok.accessors.chain=true
      return new Customer()
         .setName("nume")
         .setPhone("::phone::")
         .setAddress(new Address()
            .setCity("oras"));
   }
}
