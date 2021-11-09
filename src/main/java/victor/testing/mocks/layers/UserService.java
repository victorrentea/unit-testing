package victor.testing.mocks.layers;

import lombok.RequiredArgsConstructor;
import lombok.Value;

//your domain logic
@RequiredArgsConstructor
public class UserService {
   private final Adapter adapter;


   public void method() {
      User user = adapter.findUserByUsername("jdoe");
//      repo.save(user);
   }
}

@Value
class User {
   String username;
   String fullName;
}
