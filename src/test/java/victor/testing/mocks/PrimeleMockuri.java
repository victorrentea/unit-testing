package victor.testing.mocks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

//@RunWith(MockitoJUnitRunner.class)
public class PrimeleMockuri {

//   @Mock
   Incomoda incomoda = Mockito.mock(Incomoda.class);

   @Test
   public void test() {
      Mockito.when(incomoda.cevaExtern()).thenReturn(2);
      UserService deTestat = new UserService(incomoda);
      assertEquals(3, deTestat.deBiz());;
   }
}

class UserService {
   private final Incomoda incomoda;
   UserService(Incomoda incomoda) {
      this.incomoda = incomoda;
   }
   public int deBiz() {
       return incomoda.cevaExtern() + 1;
   }
}
class Incomoda {
   public int cevaExtern() {
       throw new IllegalArgumentException(" nu pot fi chemat din teste");
   }
}