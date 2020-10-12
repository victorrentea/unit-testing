package victor.testing.fixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountServiceTestBase { //50-60% din toata logica aplicatiei
   @BeforeEach
   public final void persistProfile() {
      System.out.println("In AccountService Before Persist user profile");
   }
}
class RegularAccountTest extends AccountServiceTestBase {
   @Test
   public void whenItIsAddedToMyProfile_theCNPIsChecked() {

   }

}

class InternationalAccountTestBase extends AccountServiceTestBase {
   @BeforeEach
   public void persistClient() {
      System.out.println("In International Account Persist client");
   }

}
class InternationalAccountForCorporateCustomer extends InternationalAccountTestBase {
   @Test
   public void whenItIsAddedToMyProfile_theSWIFTCodeIsChecked() {

   }

}
class InternationalAccountForPhysicalCustomer extends InternationalAccountTestBase {
   @Test
   public void whenItIsAddedToMyProfile_theSWIFTCodeIsChecked() {

   }
}



