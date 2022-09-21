package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

// ObjectMother F... https://martinfowler.com/bliki/ObjectMother.html
class TestData {

    public static Customer john() { // too high coupling!!
        return new Customer()
                .setName("::name::")
                .setEmail("::name::")
                .setAddress(new Address()
                        .setCity("::city::"));
    }
}

public class CustomerValidatorShould {
    CustomerValidator validator = new CustomerValidator();
    private final Customer customer = TestData.john();


    @Test
    void acceptAValidCustomer() {
        validator.validate(customer);
    }

    @Test
    void throwsForNullName() {
        customer.setName(null);

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

    @Test
    void throwsForNullEmail() {
        customer.setEmail(null);

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }
    @Test
    void throwsForNullCity() {
        customer.getAddress().setCity(null);

        Assert.assertThrows(IllegalArgumentException.class, () ->
                validator.validate(customer));
    }

}