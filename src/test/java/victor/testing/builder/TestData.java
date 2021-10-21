package victor.testing.builder;

   public class TestData {
      public static Customer aValidCustomer() {
         return new Customer()
            .setName("with")
            .setEmail("with")
            .setAddress(new Address()
               .setCity("Iasi"));
   }
}
