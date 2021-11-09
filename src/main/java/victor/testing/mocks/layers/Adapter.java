package victor.testing.mocks.layers;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class Adapter {
   private final ExternalSystem externalSystem;
   public User findUserByUsername(String username) {
      LdapUser ldapUser = externalSystem.findUser(username);
      String fullName = ldapUser.getFName() + " " + ldapUser.getLName().toUpperCase();
      return new User(ldapUser.getUName(), fullName);
   }
}


class ExternalSystem {
   public LdapUser findUser(String username) {
      return new LdapUser("jdoe", "Joe","Doe");
   }
}

@Value
class LdapUser {
   String uName;
   String fName;
   String lName;
}