package victor.testing.mutation;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.dbunit.Assertion.assertEquals;

class TestData {
    // PRO =
    // RISK = changes here may break dozens of tests >>> RULE: never change, but add a new method

   public static Customer johnCustomer() {
      return new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   }
}//Object Mother F...er https://martinfowler.com/bliki/ObjectMother.html

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // avoid, unless this test is NOT a unit test but a flow test.
public class CustomerValidatorShould {
    public static final String WRONG_NAME = "::nme::";
    public static final String WRONG_EMAIL = "::eil::";
    CustomerValidator validator = new CustomerValidator();
    private Customer customer = TestData.johnCustomer();

   public CustomerValidatorShould() {
        System.out.println("each test in JUnit gets a new instance of the test class > the field valiues never 'leak' between tests");
    }

    //   @BeforeEach
//   final void before() {
//      // you do NOT need builders for mutable data objects. Chainable setters instead.
//      customer = ;
//   }
    @Test
    @CaptureSystemOutput
    void valid(OutputCapture capture) {
        customer.getAddress().setCity(" Skopje ");

        validator.validate(customer);

        assertThat(capture.toString()).contains("Beer");
        assertThat(customer.getAddress().getCity()).isEqualTo("Skopje");
    }
    @Test
    void throws_forMissingName() {
        customer.setName(null);
        assertThatThrownBy(() -> validator.validate(customer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }
    @Test
    void throws_forMissingEmail() {
        customer.setEmail(null);
        assertThatThrownBy(() -> validator.validate(customer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");
    }
    @Test
    void throws_forMissingCity() {
        customer.getAddress().setCity(null);
        assertThatThrownBy(() -> validator.validate(customer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("city");
    }
    @Test
    void throws_forCityNameTooShort() {
        customer.getAddress().setCity("AB");
        assertThatThrownBy(() -> validator.validate(customer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("city too short");
    }

    @Test
    @CaptureSystemOutput
    void beer_forCorrectEmail(OutputCapture capture) {
        customer.setName(WRONG_NAME);

        validator.validate(customer);

        assertThat(capture.toString()).contains("Beer");
    }

    @Test
    @CaptureSystemOutput
    void beer_forCorrectName(OutputCapture capture) {
        // Arrange = Given = context of the test
        customer.setEmail(WRONG_EMAIL);

        // Act = When = the prod call
        validator.validate(customer);

        // Assert = Then = assertions and verifications
        assertThat(capture.toString()).contains("Beer");
    }

    @Test
    @CaptureSystemOutput
    void noBeer_forWrongEmailAndName(OutputCapture capture) {
        customer.setName(WRONG_NAME)
                .setEmail(WRONG_EMAIL);

        validator.validate(customer);

        assertThat(capture.toString()).doesNotContain("Beer");
    }

}