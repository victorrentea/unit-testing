package victor.testing.mocks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import victor.testing.builder.Customer;
import victor.testing.builder.CustomerValidator;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PrimeleMockuri {

   @Mock
   private Incomoda incomoda;// = Mockito.mock(Incomoda.class);
//   @Mock
//   private CustomerValidator validator;// = Mockito.mock(Incomoda.class);
   @InjectMocks
   private UserService deTestat;// = new UserService(incomoda);
   @Test
   public void test() {
      Mockito.when(incomoda.cevaExtern()).thenReturn(2);
      assertEquals(3, deTestat.deBiz());;
   }
}
class UserService {
   private final Incomoda incomoda;
   private final CustomerValidator validator;

   UserService(Incomoda incomoda, CustomerValidator validator) {
      System.out.println("val="+validator);
      this.incomoda = incomoda;
      this.validator = validator;
   }
   public int deBiz() {
      validator.validate(new Customer());
       return incomoda.cevaExtern() + 1;
   }
}

class Incomoda {
   public int cevaExtern() {
       throw new IllegalArgumentException(" nu pot fi chemat din teste");
   }
}