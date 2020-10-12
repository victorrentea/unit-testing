package victor.testing.fixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;


class WithProfileExtension implements BeforeEachCallback {
   private final String name;
   public WithProfileExtension(String name) {
      this.name = name;
   }

   @Override
   public void beforeEach(ExtensionContext context) throws Exception {
      System.out.println("In AccountService Before Persist user profile " + name);
   }
}


public class AccountServiceTestBase { //50-60% din toata logica aplicatiei
   @BeforeEach
   public final void persistProfile() {
   }
}
// niv 2 -----------------
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
/// niv 3 in ierarhie
class InternationalAccountForCorporateCustomer extends InternationalAccountTestBase {

   @RegisterExtension
   public WithProfileExtension profileExtension = new WithProfileExtension("NAME");

//   @RegisterExtension
//   public WithSwiftCodeServiceMock swiftCodeServiceMock = new WithSwiftCodeServiceMock("BIC1234");

   @Test
   public void whenItIsAddedToMyProfile_theSWIFTCodeIsChecked() {

   }
   @Test
   public void whenItIsAddedToMyProfile_theSWIFTCodeIsChecked2() {

   }

}
class InternationalAccountForPhysicalCustomer extends InternationalAccountTestBase {
   @Test
   public void whenItIsAddedToMyProfile_theSWIFTCodeIsChecked() {

   }
}



