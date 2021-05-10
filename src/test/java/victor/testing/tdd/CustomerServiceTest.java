package victor.testing.tdd;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class CustomerServiceTest {

   @Test
   public void createCustomer() {

      Customer customer = new CustomerService().createCustomer();

//
//      Assertions.assertNotNull(customer.getCreateDate());


      assertThat(customer.getCreateDate())
          .isCloseTo(now(), byLessThan(1, SECONDS));
   }
}