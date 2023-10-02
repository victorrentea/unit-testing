package victor.testing.design.app.repo;

import victor.testing.design.app.domain.Customer;

public interface CustomerRepo {
   Customer findById(long customerId);
   int countByEmail(String email);

   Long save(Customer customer);
}
