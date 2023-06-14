package victor.testing.design.objectmother;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Data
class Customer {
   String name;
   String shippingAddress;
   String email;
   String billingAddress;
   // 20 more fields...
}