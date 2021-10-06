package victor.testing.mocks.purity;

import victor.testing.builder.Customer;

public interface CustomerRepo {
   Customer findById(long customerId);
}
