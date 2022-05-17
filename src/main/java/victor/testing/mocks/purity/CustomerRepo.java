package victor.testing.mocks.purity;

import victor.testing.mutation.Customer;

public interface CustomerRepo {
   Customer findById(long customerId);
   int countByEmail(String email);

   Long save(Customer customer);
}
