package victor.testing.repo;

import victor.testing.entity.Customer;

public interface EmailClient {
   void sendWelcomeEmail(Customer customer);

   void sendNewCouponEmail(Customer customer);
}
