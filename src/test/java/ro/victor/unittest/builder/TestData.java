package ro.victor.unittest.builder;


// Object Mother F...
public class TestData {
   // dummy data for test
   public static Customer aValidCustomer() {
      return new Customer()
         .setName("")
         .setPhone("phone")
         .setAddress(aValidAddress());
   }

   public static Address aValidAddress() {
      return new Address()
         .setCity("Iasi")
         .setStreetName("Stefan")
         .setStreetNumber(19);
   }
}
