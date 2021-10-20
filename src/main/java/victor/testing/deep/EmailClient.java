package victor.testing.deep;

import victor.testing.builder.Customer;

public interface EmailClient {
   void sendWelcomeEmail(Customer customer);

   void sendNewCouponEmail(Customer customer);
}
