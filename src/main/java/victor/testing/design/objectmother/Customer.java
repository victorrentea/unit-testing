package victor.testing.design.objectmother;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
class Customer {
   @NonNull
   String name;
   @NonNull
   String shippingAddress;
   @NonNull
   String billingAddress;
   // 20 more fields...
}