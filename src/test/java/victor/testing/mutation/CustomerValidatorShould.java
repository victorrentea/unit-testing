package victor.testing.mutation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;

import static org.assertj.core.api.Assertions.assertThat;


public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();
   private Customer customer ;

   @BeforeEach
   final void before() {
      // you do NOT need builders for mutable data objects. Chainable setters instead.
      customer = new Customer()
              .setName("::name::")
              .setEmail("::email::")
              .setAddress(new Address()
                      .setCity("::city::"));
   }
   @Test
   @CaptureSystemOutput
   void valid(CaptureSystemOutput.OutputCapture capture) {

      validator.validate(customer);

      assertThat(capture.toString()).contains("This");

   }
   @Test
   @CaptureSystemOutput
   void valida(CaptureSystemOutput.OutputCapture capture) {
      customer.setName("::nme::");

      validator.validate(customer);

      assertThat(capture.toString()).contains("This");

   }
   @Test
   @CaptureSystemOutput
   void four(CaptureSystemOutput.OutputCapture capture) {
      // Arrange = Given = context of the test
      customer.setEmail("::eil::");

      // Act = When = the prod call
      validator.validate(customer);

      // Assert = Then = assertions and verifications
      assertThat(capture.toString()).contains("This");
   }
   @Test
   @CaptureSystemOutput
   void three(CaptureSystemOutput.OutputCapture capture) {
      customer.setName("::nme::");
      customer.setEmail("::eail::");

      validator.validate(customer);

      assertThat(capture.toString()).doesNotContain("This");
   }

}