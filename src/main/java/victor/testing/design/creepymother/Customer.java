package victor.testing.design.creepymother;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
class Customer {
//   @NonNull
   String name;
//   @NonNull
   String shippingAddress;
//   @NonNull
   String billingAddress;
   // 20 more fields...
}


//@Value
//@Builder
//class ShippingCustomer {
//   //   @NonNull
//   String name;
//   //   @NonNull
//   String shippingAddress;
//   // 20 more fields...
//}
//@Value
//@Builder
//class BillingCustomer {
//   //   @NonNull
//   String name;
//   //   @NonNull
//   String billingAddress;
//   // 20 more fields...
//}