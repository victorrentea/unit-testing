package victor.testing.design.objectmother;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import static java.util.Objects.requireNonNull;

@Value
@Builder
@With
class Customer {
   @NonNull
   String name;
   @NonNull
   String shippingAddress;
   @NonNull
   String billingAddress;
   // 20 more fields...
}