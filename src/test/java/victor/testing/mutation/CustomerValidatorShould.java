package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;


public class CustomerValidatorShould {
    CustomerValidator validator = new CustomerValidator();


    @Test
    void acceptAValidCustomer() {
        Customer customer = aValidCustomer();
        validator.validate(customer);
    }

    private static Customer aValidCustomer() {
        return new Customer()
                .setName("::name::")
                .setEmail("::name::")
                .setAddress(new Address()
                        .setCity("::city::"));
    }

    @Test
    void throwsForNullName() {
        Customer customer = aValidCustomer().setName(null);

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

    @Test
    void throwsForNullEmail() {
        Customer customer = aValidCustomer().setEmail(null);

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }
    @Test
    void throwsForNullCity() {
        Customer customer = aValidCustomer();
        customer.getAddress().setCity(null);

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

}