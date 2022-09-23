package victor.testing.nested;

import victor.testing.mutation.Customer;

public interface EmailClient {
   void sendWelcomeEmail(Customer customer);

   void sendNewCouponEmail(Customer customer);
}
