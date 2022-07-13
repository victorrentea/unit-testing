package victor.testing.mutation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import victor.testing.tools.CaptureSystemOutput;
import victor.testing.tools.CaptureSystemOutput.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;


public class CustomerValidatorShould {
    CustomerValidator validator = new CustomerValidator();
    private Customer customer = new Customer()
            .setName("::name::")
            .setEmail("::email::")
            .setAddress(new Address()
                    .setCity("::city::"));

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

        validator.validate(customer);

        assertThat(capture.toString()).contains("This");

    }

    @Test
    @CaptureSystemOutput
    void valida(OutputCapture capture) {
        customer.setName("::nme::");

        validator.validate(customer);

        assertThat(capture.toString()).contains("This");

    }

    @Test
    @CaptureSystemOutput
    void four(OutputCapture capture) {
        // Arrange = Given = context of the test
        customer.setEmail("::eil::");

        // Act = When = the prod call
        validator.validate(customer);

        // Assert = Then = assertions and verifications
        assertThat(capture.toString()).contains("This");
    }

    @Test
    @CaptureSystemOutput
    void three(OutputCapture capture) {
        customer.setName("::nme::")
                .setEmail("::eail::");

        validator.validate(customer);

        assertThat(capture.toString()).doesNotContain("This");
    }

}