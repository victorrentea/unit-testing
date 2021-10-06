package victor.testing.mocks.purity.repo;

import victor.testing.mocks.purity.domain.Customer;

public interface CustomerRepo {
   Customer findById(long customerId);
}
