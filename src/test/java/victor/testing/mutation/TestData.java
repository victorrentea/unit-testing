package victor.testing.mutation;

// Object Mother design pattern
// https://martinfowler.com/bliki/ObjectMother.html
// da obiecte gata facute "standard"
public class TestData {
//  public static Customer.Builder validCustomer() { + @Builder daca Customer e immutable
  public static Customer validCustomer() {
     return new Customer()
         .setName("::name::")
         .setEmail("::email::") // nu se modifica LINIA ci adaugi linii sau metode noi
         .setAddress(new Address()
             .setCity("::city::"));
  }
}
// la noi problema e ca anumite teste se specializeaza pe ANUMITE zone diferite
//
