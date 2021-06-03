package victor.testing.builder;

import org.junit.Test;

public class CustomerValidatorTest {

   private CustomerValidator validator = new CustomerValidator();

   @Test
   public void yesSir() {

      validator.validate(new Customer()
          .setName("John")
          .setAddress(new Address()
				 .setCity("Bucuresti. Orasul smogului.")));
   }

}