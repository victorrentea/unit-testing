package victor.testing.repo;

import victor.testing.entity.Customer;

public interface CustomerRepo {
   int countByEmail(String email);
   Customer save(Customer customer);
}
