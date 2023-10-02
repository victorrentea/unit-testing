package victor.testing.design.objectmother;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static java.util.Objects.requireNonNull;

@Value
@Builder(toBuilder = true)
public class Customer {
   @NonNull
   String name;
   @NonNull
   String shippingAddress;
   @NonNull
   String billingAddress;
   // 20 more fields...
}