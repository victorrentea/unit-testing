package victor.testing.mutation;

import lombok.Builder;
import lombok.With;
import org.junit.jupiter.api.Test;
import victor.testing.TestData;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class CustomerValidatorTest {
  CustomerValidator validator = new CustomerValidator();


  @Builder
  @With
  public record CustomerRec(
      Long id,
      String name,
      String email,
      List<String> labels,
      Address address,
      Date createDate,
      List<Coupon> coupons) {
    public CustomerRec withId(Long id) {
      return this.id == id ? this : new CustomerRec(id, name, email, labels, address, createDate, coupons);
    }

    public CustomerRec withName(String namep) {
      return this.name == namep ? this : new CustomerRec(id, namep, email, labels, address, createDate, coupons);
    }

  }


  @Test
  void valid() {
    // #3 Builder for immutable
//    CustomerRec c = CustomerRec.builder()
//        .name("::name::")
//        .email("::email::")
//        .build();

    //#2: Withers for immutable
//    CustomerRec customerWithNullName = c.withName(null);
    // if you had some required args mandated by the constructor, build allows you to cause a runtime ex
    // rather than a compilation failure.

    // #1 chainable setters for mutable data
    Customer aCustomer = TestData.aCustomer();
    validator.validate(aCustomer);
  }
  @Test
  void throwForMissingName() {
    Customer aCustomer = TestData.aCustomer().setName(null);
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(aCustomer));
  }
  @Test
  void throwForMissingEmail() {
    Customer aCustomer = TestData.aCustomer().setEmail(null);
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(aCustomer));
  }
  @Test
  void throwForBlankEmail() {
    Customer aCustomer = TestData.aCustomer().setEmail("");
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(aCustomer));
  }
  @Test
  void throwForMissingAddressCity() {
    Customer aCustomer = TestData.aCustomer()
//        .setAddress(new Address()) // heavy

//        .setAddress(TestData.aValidCustomer().getAddress()
//            .setCity(null)); // cumbersome for deeper nested

        .setAddress(TestData.anAddress().setCity(null));

    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(aCustomer));
  }

  @Test
  void throwForAddressCityTooShort() {
    Customer aCustomer = TestData.aCustomer()
        .setAddress(TestData.anAddress().setCity("AB"));

    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> validator.validate(aCustomer));
  }


  @Test
  void trimCityBeforeValidation() {
    Customer aCustomer = TestData.aCustomer()
        .setAddress(TestData.anAddress().setCity("  ABC  ")); // 4+ chars with spaces

    validator.validate(aCustomer);

    assertThat(aCustomer.getAddress().getCity()).isEqualTo("ABC");
  }
}