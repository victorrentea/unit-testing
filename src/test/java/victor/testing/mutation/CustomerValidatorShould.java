package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static victor.testing.mutation.TestData.anAddress;
import static victor.testing.mutation.TestData.john;

public class CustomerValidatorShould {
   CustomerValidator validator = new CustomerValidator();

    @Test
    void acceptAValidCustomer() {
        Customer customer = john().build();
        validator.validate(customer);
    }
    // overtesting
//    @Test
//    void throwsForNullCustomer() {
//        Assert.assertThrows(IllegalArgumentException.class, () ->
//                validator.validate(null));
//    }

    @Test
    void throwsForNullName() {
        Customer customer = john().name(null).build();

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

    @Test
    void throwsForNullEmail() {
        Customer customer = john().email(null).build();

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }
    @Test
    void throwsForNullCity() {
        Customer customer = john()
                .address(anAddress().city(null).build())
                .build();

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }
    @Test
    void throwsTooShortCity() {
        Customer customer = john()
                .address(anAddress().city("Oi").build())
                .build();

        IllegalArgumentException e = Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));

        // is this a good idea ? only if the messages are constants somewhere.
        assertThat(e.getMessage()).isEqualTo("Address city too short"); // asserting a presentation concern. bad idea.

    }

    @Test
    void trimsCityName() {
        Customer customer = john()
                .address(anAddress().city(" Pui ").build())
                .build();

        validator.validate(customer);

        assertThat(customer.getAddress().getCity()).isEqualTo("Pui");
    }
}