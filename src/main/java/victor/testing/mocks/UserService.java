package victor.testing.mocks;

import lombok.RequiredArgsConstructor;
import victor.testing.builder.Customer;
import victor.testing.builder.CustomerValidator;

@RequiredArgsConstructor
public class UserService {
   private final Incomoda incomoda;
   private final CustomerValidator validator;

   public int deBiz(Customer customer) {
      validator.validate(customer);
      return incomoda.cevaExtern() + 1;
   }
}
class Incomoda {
   public int cevaExtern() {
      throw new IllegalArgumentException(" nu pot fi chemat din teste");
   }
}