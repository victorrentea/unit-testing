package victor.testing.mutation;

// Object Mother Pattern pt ca Don't Repeat Yourself
public class TestData {
  // o metoda static ce produce o instanta 'standard'
  // folosita de 120 de @Test
  public static Customer aCustomer(/*String email*/) {
    return new Customer()
        .setName("::name::")
        .setEmail("::email::") // lombok poate genera setteri sa intoarca 'this'. vezi lombok.config din src/main/java
        .setAddress(new Address()
            .setCity("::city::")
            .setCountry(Country.ROU));
  }
  // RISK : cand depind multi de o chestie, si chestia se schimba -> BUM
  // BEST PRACTICE: doar adaugi noi metode sau linii de cod. nu modific niciodata
  // evita param la aceste metode.
}
