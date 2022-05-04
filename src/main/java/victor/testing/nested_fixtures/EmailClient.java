package victor.testing.nested_fixtures;

import victor.testing.builder.Customer;

public interface EmailClient {
   void sendWelcomeEmail(Customer customer);

   void sendNewCouponEmail(Customer customer);
}
