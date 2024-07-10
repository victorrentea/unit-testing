package victor.testing.design.objectmother;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value // immutable object needing all fields in constructor
@Builder
class Customer {
   @NonNull
   String name;
   @NonNull
   String shippingAddress;
   @NonNull
   String billingAddress;
   @NonNull
   String phoneNumber;
   // 20 more fields...
}