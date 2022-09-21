package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class CustomerValidatorShould {
    CustomerValidator validator = new CustomerValidator();

    @Test
    void acceptAValidCustomer() {
        Customer customer = new Customer()
                .setName("::name::")
                .setEmail("::email::")
                .setAddress(new Address()
                        .setCity("::city::"));
        validator.validate(customer);
    }

    @Test
    void throwsForNullName() {
        Customer customer = new Customer()
                .setEmail("::email::")
                .setAddress(new Address()
                        .setCity("::city::"));

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

    @Test
    void throwsForNullEmail() {
        Customer customer = new Customer()
                .setName("::name::")
                .setAddress(new Address()
                        .setCity("::city::"));

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

}