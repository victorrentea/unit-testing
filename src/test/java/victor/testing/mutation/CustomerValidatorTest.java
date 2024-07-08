package victor.testing.mutation;


import org.junit.Assert;
import org.junit.jupiter.api.Test;

// test class
public class CustomerValidatorTest {
   CustomerValidator validator = new CustomerValidator();

   @Test
   void success() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      validator.validate(customer);
   }

   @Test
   void customerNameIsNull() {
      Customer customer = new Customer();
      customer.setEmail("::email::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

   @Test
   void customerEmailIsNull() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setAddress(new Address());
      customer.getAddress().setCity("::city::");

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }
   @Test
   void customerCityIsNull() {
      Customer customer = new Customer();
      customer.setName("::name::");
      customer.setEmail("::email::");
      customer.setAddress(new Address());

      Assert.assertThrows(IllegalArgumentException.class,
          ()->validator.validate(customer));
   }

    @Test
    void cityTooShort() {
        Customer customer = new Customer();
        customer.setName("::name::");
        customer.setEmail("::email::");
        customer.setAddress(new Address());
        customer.getAddress().setCity("a");

        Assert.assertThrows(IllegalArgumentException.class,
            ()->validator.validate(customer));
    }
}